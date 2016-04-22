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
package org.server.core.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.util.HashedWheelTimer;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * 
 * nettytools 工具集
 * 
 * @author Hxms
 *         
 */
public class NettyCenter {
    
    static final Logger log = LoggerFactory.getLogger(NettyCenter.class);
                            
    /**
     * 工具类实例
     */
    static NettyCenter  instance;
                        
    /**
     * 事件循环组
     */
    EventLoopGroup      eventLoopGroup;
                        
    /**
     * 定时器
     */
    HashedWheelTimer    hashedWheelTimer;
                        
    /**
     * 简单客户端 SSl 环境
     */
    SslContext          simpleClientSslContext;
                        
    /**
     * 私有构造函数
     */
    private NettyCenter() {
        int maybeThreadSize = Runtime.getRuntime().availableProcessors();
        if (maybeThreadSize == 1) maybeThreadSize += 2;
        else if (maybeThreadSize == 8) maybeThreadSize = 2;
        else if (maybeThreadSize > 8) maybeThreadSize /= 2;
        /**
         * 构造事件循环组
         */
        eventLoopGroup = new NioEventLoopGroup(maybeThreadSize, new DefaultThreadFactory("NettyNioLoopGroup"));
        /**
         * 构造定时器
         */
        hashedWheelTimer = new HashedWheelTimer(new DefaultThreadFactory("NettyHashedWheelTimer"));
        /**
         * 构造 SSL 环境
         */
        try {
            SslContextBuilder sslContextBuilder = SslContextBuilder.forClient();
            sslContextBuilder.clientAuth(ClientAuth.OPTIONAL);
            simpleClientSslContext = sslContextBuilder.build();
        } catch (Throwable e) {
            log.error("NettyCenter :: initialize client sslcontext error!", e);
        }
    }
    
    /**
     * 获得事件循环组
     * 
     * @return 事件循环组
     */
    public EventLoopGroup getEventLoopGroup() {
        return eventLoopGroup;
    }
    
    /**
     * 获得定时器对象
     * 
     * @return 定时器对象
     */
    public HashedWheelTimer geTimer() {
        return hashedWheelTimer;
    }
    
    /**
     * 创建新的 Bootstrap
     * 
     * @return
     */
    public Bootstrap newBootstrap() {
        Bootstrap bootstrap = new Bootstrap();
        // config event loop group
        bootstrap.group(getEventLoopGroup());
        // config channel
        bootstrap.channel(NioSocketChannel.class);
        // set tcp flag
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        return bootstrap;
    }
    
    /**
     * 创建服务 Bootstrap 对象
     * 
     * @return
     */
    public ServerBootstrap newServerBootstrap() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(getEventLoopGroup());
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.option(ChannelOption.TCP_NODELAY, true);
        serverBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        return serverBootstrap;
    }
    
    /**
     * 获得简单客户端 SSl 访问环境
     * 
     * @return the simpleClientSslContext 实例
     */
    public SslContext getSimpleClientSslContext() {
        return simpleClientSslContext;
    }
    
    /**
     * 获得实例
     * 
     * @return 获得实例
     */
    public synchronized static NettyCenter singleInstance() {
        if (instance == null) instance = new NettyCenter();
        return instance;
    }
}
