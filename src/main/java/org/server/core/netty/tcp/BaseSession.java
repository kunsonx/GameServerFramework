/**
 * Copyright 2015年11月20日 Hxms.
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
package org.server.core.netty.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.function.Consumer;

import org.server.core.netty.NettyCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Netty 连接件
 * 
 * @author Hxms
 *         
 */
abstract class BaseSession implements AutoCloseable {
    
    /**
     * 事件监听适配器
     * 
     * @author Hxms
     *         
     */
    class ChannelEventListen extends ChannelInboundHandlerAdapter {
        
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            channalActive0((NioSocketChannel) ctx.channel());
        }
        
    }
    
    /**
     * 异常处理适配器
     * 
     * @author Hxms
     *         
     */
    class ExceptionHandlerAdapter extends ChannelHandlerAdapter {
        
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            safeCallHandle(h -> h.onException(cause));
        }
    }
    
    static final Logger log = LoggerFactory.getLogger(BaseSession.class);
                            
    /**
     * Bootstrap
     */
    Bootstrap           bootstrap;
                        
    /**
     * 表示链接件唯一连接
     */
    NioSocketChannel    channel;
                        
    /**
     * 事件处理对象
     */
    BaseSessionEvent    handler;
                        
    /**
     * 创建 tcp 链接件
     */
    public BaseSession() {
        createBootstrap();
    }
    
    /**
     * 创建 Bootstrap
     */
    void createBootstrap() {
        // create a new bootstrap
        bootstrap = NettyCenter.singleInstance().newBootstrap();
        // config bootstrap
        safeCallHandle(h -> h.configBootstrap(bootstrap));
        // coifig initialize handler
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                configNewChannel(ch);
            }
        });
    }
    
    /**
     * 配置 nio socket channel
     * 
     * @param channel
     *            通信频道
     */
    protected void configNewChannel(NioSocketChannel channel) {
        // 获得 pipeline
        ChannelPipeline pipeline = channel.pipeline();
        // 异常处理起
        pipeline.addLast(new ExceptionHandlerAdapter());
        // 添加标准处理
        pipeline.addLast(new ChannelEventListen());
        // 通知事件处理
        safeCallHandle(h -> h.configNewChannel(channel));
    }
    
    /**
     * 设置连接超时时间
     * 
     * @param millis
     *            毫秒数
     */
    public void setConnectTimeoutMillis(int millis) {
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, millis);
    }
    
    /**
     * 获得会话处理事件
     * 
     * @return the handler 会话处理事件对象
     */
    public BaseSessionEvent getHandler() {
        return handler;
    }
    
    /**
     * 设置会话处理事件
     * 
     * @param handler
     *            the handler to set 处理事件对象
     */
    public void setHandler(BaseSessionEvent handler) {
        this.handler = handler;
    }
    
    /**
     * 频道可用事件
     * 
     * @param channel
     *            频道
     */
    protected void channalActive0(NioSocketChannel channel) {
    }
    
    /**
     * 关闭之前频道
     */
    protected void closeChannel() {
        if (channel != null) channel.close();
        channel = null;
    }
    
    /**
     * 安全调用 handler
     * 
     * @param consumer
     *            调用函数
     */
    protected void safeCallHandle(Consumer<BaseSessionEvent> consumer) {
        if (handler != null) {
            try {
                consumer.accept(handler);
            } catch (Throwable e) {
                log.error("BaseSession::Callback . ", e);
            }
        }
    }
}
