package org.server.core.netty.tcp;

import java.util.Objects;

import org.server.tools.Mission;
import org.server.core.netty.NettyCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NServer implements AutoCloseable {
    
    static final Logger    log = LoggerFactory.getLogger(NServer.class);
                               
    ServerBootstrap        serverBootstrap;
    NioServerSocketChannel serverChannel;
    Mission                bindMission;
    ChannelFuture          bindFuture;
                           
    /**
     * 构造新服务器实例
     */
    public NServer(ChannelInitializer<SocketChannel> childChannelInitialize) {
        Objects.requireNonNull(childChannelInitialize, "child channel handler not set.");
        
        serverBootstrap = NettyCenter.singleInstance().newServerBootstrap();
        bindMission = new Mission();
        
        serverBootstrap.childHandler(childChannelInitialize);
    }
    
    /**
     * 获得服务引导对象
     * 
     * @return the serverBootstrap
     */
    public ServerBootstrap getServerBootstrap() {
        return serverBootstrap;
    }
    
    /**
     * 获得服务频道
     * 
     * @return the serverChannel
     */
    public NioServerSocketChannel getServerChannel() {
        return serverChannel;
    }
    
    /**
     * 绑定端口
     * 
     * @param bindPort
     *            端口号
     * @return 绑定操作完成
     */
    public boolean bind(int bindPort) {
        if (bindFuture != null) return false;
        ChannelFutureListener listener = this::bindOperationComplete;
        bindFuture = serverBootstrap.bind(bindPort);
        bindMission.newMission();
        bindFuture.addListener(listener);
        return true;
    }
    
    /**
     * 等待连接完成
     */
    public void waitBindDone() {
        bindMission.awaitMission();
    }
    
    /**
     * 返回是否在绑定端口中
     * 
     * @return 标识符
     */
    public boolean isBinding() {
        return bindFuture != null;
    }
    
    /**
     * 返回是否绑定端口成功
     * 
     * @return 标识
     */
    public boolean isBindSuccess() {
        return serverChannel != null;
    }
    
    /**
     * 关闭服务监听频道
     */
    public void closeServerChannel() {
        if (serverChannel != null) serverChannel.close();
        serverChannel = null;
    }
    
    void bindOperationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
            serverChannel = (NioServerSocketChannel) future.channel();
        }
        bindFuture = null;
        bindMission.missionFinish();
    }
    
    @Override
    public void close() throws Exception {
        closeServerChannel();
    }
}
