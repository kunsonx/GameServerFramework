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
package org.server.core.netty.tcp;

import java.net.InetSocketAddress;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 
 * 抽象 tcp 连接件
 * 
 * @author Hxms
 *         
 */
class BaseConnector extends BaseSession {
	
	/**
	 * 连接操作返回对象
	 */
	ChannelFuture connectFuture;
	
	/**
	 * 连接操作检查
	 * 
	 * @return 连接操作
	 */
	protected ChannelFuture validConnectTask() {
		if (connectFuture == null) return null;
		ChannelFuture future = connectFuture;
		connectFuture = null;
		return future;
	}
	
	/**
	 * 连接操作完成事件
	 */
	protected void connectFutureFinish(ChannelFuture future) {
		future = validConnectTask();
		if (future != null) {
			connectFinalCallback(future, 0);
		}
	}
	
	/**
	 * 
	 * 调用连接完成回调
	 * 
	 * @param future
	 *            操作事件
	 * 
	 * @param retryCount
	 *            重试次数
	 */
	protected void connectFinalCallback(ChannelFuture future, int retryCount) {
		// close before channel
		closeChannel();
		// assignment new channel
		channel = (NioSocketChannel) future.channel();
		// call the event
		safeCallHandle(h -> h.onConnectFinish(future.isSuccess(), channel, future.cause(), retryCount));
	}
	
	/**
	 * 开始连接
	 * 
	 * @param hostname
	 *            域名
	 * @param port
	 *            端口
	 */
	public boolean connect(String hostname, int port) {
		return connect(new InetSocketAddress(hostname, port));
	}
	
	/**
	 * 开始连接函数
	 * 
	 * @param socketAddress
	 *            连接地址
	 * @return 连接操作是否已经开始
	 */
	public boolean connect(InetSocketAddress socketAddress) {
		if (connectFuture != null) return false;
		ChannelFutureListener listener = this::connectFutureFinish;
		connectFuture = bootstrap.connect(socketAddress);
		connectFuture.addListener(listener);
		return true;
	}
	
	/**
	 * 等待连接完成
	 * 
	 * @throws InterruptedException
	 */
	public void waitConnect() throws InterruptedException {
		if (connectFuture != null) connectFuture.sync();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		closeChannel();
	}
}
