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
import io.netty.channel.socket.nio.NioSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * tcp 连接处理
 * 
 * @author Hxms
 *         
 */
public class BaseSessionEvent {
    
    static final Logger log = LoggerFactory.getLogger(BaseSessionEvent.class);
    
    /**
     * 配置 bootstrap
     * 
     * @param bootstrap
     */
    public void configBootstrap(Bootstrap bootstrap) {
    }
    
    /**
     * 配置 nio socket channel
     * 
     * @param channel
     *            通信频道
     */
    public void configNewChannel(NioSocketChannel channel) {
    }
    
    /**
     * 连接操作完成事件
     * 
     * @param success
     *            操作是否成功
     * @param channel
     *            频道
     * @param LastThrowable
     *            最后一次发生的异常
     * @param retryCount
     *            重试次数
     */
    public void onConnectFinish(boolean success, NioSocketChannel channel, Throwable LastThrowable, int retryCount) {
    }
    
    /**
     * 异常处理函数
     * 
     * @param exception
     *            异常
     */
    public void onException(Throwable exception) {
        log.warn("BaseSession onException ::", exception);
    }
}
