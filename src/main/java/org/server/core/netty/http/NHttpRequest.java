/**
 * Copyright 2015年11月21日 Hxms.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package org.server.core.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.Timeout;
import io.netty.util.internal.StringUtil;

import static org.server.core.netty.http.NHttpRequestConst.*;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLEngine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.server.tools.Mission;
import org.server.core.netty.NettyCenter;
import org.server.core.netty.tcp.BaseSessionEvent;
import org.server.core.netty.tcp.NConnector;

/**
 * 
 * netty http 异步客户端
 * 
 * @author Hxms
 *         
 */
public class NHttpRequest {
    
    static final Logger log            = LoggerFactory.getLogger(NHttpRequest.class);
                                       
    /**
     * tcp 客户端
     */
    NConnector          client;
                        
    /**
     * 请求配置表
     */
    NHttpRequestParams  requestConfig;
                        
    /**
     * 请求任务
     */
    Mission             requestMission = new Mission();
                                       
    /**
     * 请求超时计时器
     */
    Timeout             requestTimeout;
                        
    /**
     * 请求响应
     */
    NHttpResponse       response;
                        
    /**
     * 创建新请求对象
     * 
     * @param requestConfig
     *            请求配置文件
     */
    public NHttpRequest(NHttpRequestParams requestConfig) {
        super();
        this.requestConfig = requestConfig;
    }
    
    /**
     * 请求响应内容
     * 
     * @return the response
     */
    public NHttpResponse getResponse() {
        return response;
    }
    
    /**
     * 开始请求
     * 
     * @return 操作是否成功
     */
    public boolean request() {
        int missionCode = requestMission.getMissionCode();
        int newCode = requestMission.newMission();
        if (missionCode == newCode) { return false; }
        // initialize client
        client = new NConnector();
        // config handler
        client.setHandler(new NHttpClientHandler());
        // config option
        client.setConnectTimeoutMillis(requestConfig.connectTimeout());
        // enable retry connect
        if (requestConfig.retryConnectCount() > 0) client.enableRetryConnect(requestConfig.retryConnectCount());
        // begin connect
        client.connect(requestConfig.hostname(), requestConfig.port());
        return true;
    }
    
    /**
     * 等待请求完成
     */
    public void waitRequest() {
        requestMission.awaitMission();
    }
    
    /**
     * 发送 http 请求
     */
    void sendRequest() {
        Object[] httpObjects = {};
        try {
            httpObjects = requestConfig.buildRequest();
        } catch (Exception ex) {
            log.error("NettyHttpRequest :: error of encoder post data !", ex);
        }
        // 发送请求
        for (Object httpObject : httpObjects)
            client.getChannel().writeAndFlush(httpObject);
        // 设定超时
        requestTimeout = NettyCenter.singleInstance().geTimer().newTimeout(this::onRequestTimeout,
                requestConfig.requestTimeout(), TimeUnit.MILLISECONDS);
    }
    
    /**
     * 请求超时事件
     * 
     * @param timeout
     *            超时对象
     */
    void onRequestTimeout(Timeout timeout) {
        finalCallback(NE_REQUEST_TIMEOUT, null, null);
    }
    
    /**
     * 最终回调
     * 
     * @param callback
     *            回调执行
     */
    void finalCallback(int errorCode, HttpResponse response, String content) {
        String responseText = "";
        if (!clearResource()) return;
        if (response != null) {
            responseText = response.toString();
            responseText = responseText
                    .substring(responseText.indexOf(StringUtil.NEWLINE) + StringUtil.NEWLINE.length());
        }
        this.response = new NHttpResponse(responseText, content, response != null ? response.getStatus().code() : 0,
                errorCode);
        NHttpRequestCallback cb = requestConfig.callback();
        if (cb != null) try {
            cb.callback(errorCode, response, content);
        } catch (Throwable e) {
            log.error("NettyHttpRequest::Callback. ", e);
        }
        requestMission.missionFinish();
    }
    
    /**
     * 清理资源
     */
    synchronized boolean clearResource() {
        if (client != null) {
            // cleanup client
            client.disconnect();
            client = null;
            // cleanup timeout
            if (requestTimeout != null) requestTimeout.cancel();
            requestTimeout = null;
            return true;
        }
        return false;
    }
    
    class NHttpClientHandler extends BaseSessionEvent {
        
        /*
         * (non-Javadoc)
         * 
         * @see
         * com.hxms.wx.client.netty.tcp.TcpClientHandler#configNewChannel(io
         * .netty .channel.socket.nio.NioSocketChannel)
         */
        @Override
        public void configNewChannel(NioSocketChannel channel) {
            super.configNewChannel(channel);
            ChannelPipeline pipeline = channel.pipeline();
            // 添加 SSL 数据支持
            if (requestConfig.https()) {
                SslContext sslContent = NettyCenter.singleInstance().getSimpleClientSslContext();
                SSLEngine engine = sslContent.newEngine(channel.alloc());
                pipeline.addLast("ssl", new SslHandler(engine));
            }
            // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
            pipeline.addLast("decoder", new HttpResponseDecoder());
            // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
            pipeline.addLast("encoder", new HttpRequestEncoder());
            // 接收的请求累计器
            pipeline.addLast("aggegator", new HttpObjectAggregator(0x30000));
            // mime 类型写出
            pipeline.addLast("streamew", new ChunkedWriteHandler());
            // 添加解压器
            pipeline.addLast("decompressor", new HttpContentDecompressor());
            // add new handler
            pipeline.addLast("handler", new NettyHttpRequestChannelHandler());
        }
        
        /*
         * (non-Javadoc)
         * 
         * @see
         * com.hxms.wx.client.netty.tcp.TcpClientHandler#onConnectFinish(boolean
         * , io.netty.channel.socket.nio.NioSocketChannel, java.lang.Throwable,
         * int)
         */
        @Override
        public void onConnectFinish(boolean success, NioSocketChannel channel, Throwable LastThrowable,
                int retryCount) {
            // 连接不成功回调
            if (!success) finalCallback(NE_CONNECT_FAIL, null, null);
        }
    }
    
    /**
     * http request channel handler class
     * 
     * @author Hxms
     *         
     */
    class NettyHttpRequestChannelHandler extends SimpleChannelInboundHandler<HttpObject> {
        
        HttpResponse  response;
        String        content;
        StringBuilder contentBuilder;
                      
        NettyHttpRequestChannelHandler() {
            super(false);
        }
        
        /*
         * (non-Javadoc)
         * 
         * @see
         * io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty
         * .channel.ChannelHandlerContext)
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            sendRequest();
        }
        
        /*
         * (non-Javadoc)
         * 
         * @see
         * io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.
         * netty.channel.ChannelHandlerContext)
         */
        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            finalCallback(NE_REQUEST_LOST_CONNECT, null, null);
        }
        
        /*
         * (non-Javadoc)
         * 
         * @see
         * io.netty.channel.SimpleChannelInboundHandler#channelRead0(io.netty
         * .channel.ChannelHandlerContext, java.lang.Object)
         */
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
            boolean finish = false;
            // block http message
            if (msg instanceof HttpResponse) {
                response = (HttpResponse) msg;
                // full http message
                if (response instanceof FullHttpResponse) {
                    FullHttpResponse rsp = (FullHttpResponse) msg;
                    content = decodeText(rsp.content());
                    finish = true;
                } else {
                    contentBuilder = new StringBuilder();
                }
            } else if (msg instanceof HttpContent) {
                // final block
                if (msg instanceof LastHttpContent) {
                    finish = true;
                    if (contentBuilder != null) content = contentBuilder.toString();
                } else {
                    // content block
                    HttpContent content = (HttpContent) msg;
                    contentBuilder.append(decodeText(content.content()));
                }
            }
            // 完成请求
            if (finish) {
                finalCallback(NE_OK, response, this.content.toString());
            }
        }
        
        /**
         * 解码 http 请求
         * 
         * @param buf
         *            缓冲区
         * @return 结果字符串
         */
        String decodeText(ByteBuf buf) {
            try {
                return buf.toString(requestConfig.responseCharset());
            } catch (Exception e) {
                log.error("decodeHtmlString ::  " + e);
            } finally {
                buf.release();
            }
            return null;
        }
    }
}
