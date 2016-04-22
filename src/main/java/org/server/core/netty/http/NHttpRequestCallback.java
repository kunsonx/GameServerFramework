/**
 * Copyright 2015年10月20日 Hxms.
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

import io.netty.handler.codec.http.HttpResponse;

/**
 * 
 * nehttp 回调函数
 * 
 * @author Hxms
 *
 */
@FunctionalInterface
public interface NHttpRequestCallback {

	/**
	 * 回调函数
	 * 
	 * @param errorCode
	 *            错误代码
	 * @param response
	 *            响应
	 * @param content
	 *            响应内容
	 */
	public void callback(int errorCode, HttpResponse response, String content) throws Exception;
}
