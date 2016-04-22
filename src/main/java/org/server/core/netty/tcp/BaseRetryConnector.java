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

import io.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;

import org.server.tools.Mission;


/**
 * 
 * tcp 自动重试连接客户端
 * 
 * @author Hxms
 *         
 */
abstract class BaseRetryConnector extends BaseConnector {
	
	boolean				retryConnect		= false,
	                            retryingConnect = false;
	int					retryConnectCount	= 0,
	                            retryConnectMaxCount = 0;
	Mission				connectMission		= new Mission();
	InetSocketAddress	lastConnectAddress;
						
	/**
	 * 
	 * 获得是否自动重试连接
	 * 
	 * @return the retryConnect
	 */
	public boolean isRetryConnect() {
		return retryConnect;
	}
	
	/**
	 * 
	 * 获得是否正在尝试重试连接
	 * 
	 * @return the retryingConnect
	 */
	public boolean isRetryingConnect() {
		return retryingConnect;
	}
	
	/**
	 * 
	 * 获得当前重试连接次数
	 * 
	 * @return the retryConnectCount
	 */
	public int getRetryConnectCount() {
		return retryConnectCount;
	}
	
	/**
	 * 
	 * 获得尝试连接最大次数
	 * 
	 * @return the retryConnectMaxCount
	 */
	public int getRetryConnectMaxCount() {
		return retryConnectMaxCount;
	}
	
	/**
	 * 启用自动重连功能
	 * 
	 * @param maxCount
	 *            重连自大次数
	 * 
	 * @return 操作是否成功
	 */
	public boolean enableRetryConnect(int maxCount) {
		if (!retryingConnect) {
			retryConnect = true;
			retryConnectCount = 0;
			retryConnectMaxCount = Math.max(maxCount, 1);
			return true;
		}
		return false;
	}
	
	/**
	 * 禁用自动重连功能
	 * 
	 * @return 操作是否成功
	 */
	public boolean disableRetryConnect() {
		if (!retryingConnect) {
			retryConnect = false;
			retryConnectCount = 0;
			retryConnectMaxCount = 0;
			return true;
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hxms.wx.client.netty.tcp.TcpClient#connect(java.net.InetSocketAddress
	 * )
	 */
	@Override
	public boolean connect(InetSocketAddress socketAddress) {
		boolean oprationSuccess = super.connect(socketAddress);
		// enable retry flag
		if (oprationSuccess && retryConnect) {
			retryingConnect = true;
			lastConnectAddress = socketAddress;
		}
		if (oprationSuccess) {
			connectMission.newMission();
		}
		return oprationSuccess;
	}
	
	@Override
	protected void connectFutureFinish(ChannelFuture future) {
		future = validConnectTask();
		if (future == null) return;
		if (!future.isSuccess() && retryingConnect && retryConnectCount < retryConnectMaxCount) {
			retryConnectCount++;
			super.connect(lastConnectAddress);
		} else {
			// call callback
			connectFinalCallback(future, retryConnectCount);
			// clear flag
			retryConnectCount = 0;
			retryingConnect = false;
			// clear save addr
			lastConnectAddress = null;
			// connect finish ...
			connectMission.missionFinish();
		}
	}
	
	@Override
	public void waitConnect() {
		connectMission.awaitMission();
	}
}
