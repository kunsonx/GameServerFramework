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

import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 
 * 表示唯一会话 tcp 客户端
 * 
 * @author Hxms
 *        
 */
public class NConnector extends BaseRetryConnector {
	
	/**
	 * 获得链接件唯一通信频道
	 * 
	 * @return 频道
	 */
	public NioSocketChannel getChannel() {
		return channel;
	}
	
	/**
	 * 客户端是否已经连接上
	 * 
	 * @return 是否连接成功
	 */
	public boolean isConnected() {
		return channel != null && channel.isActive();
	}
	
	/**
	 * 判断是否连接中
	 * 
	 * @return
	 */
	public boolean isConnecting() {
		return connectFuture != null && !connectFuture.isDone();
	}
	
	/**
	 * 断开连接
	 */
	public void disconnect() {
		closeChannel();
	}
}
