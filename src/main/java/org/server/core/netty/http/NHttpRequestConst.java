package org.server.core.netty.http;

public class NHttpRequestConst {
	/**
	 * http 请求成功
	 */
	public static final int NE_OK = 0;

	/**
	 * http 请求连接失败
	 */
	public static final int NE_CONNECT_FAIL = 1;

	/**
	 * http 请求连接超时
	 */
	public static final int NE_CONNECT_TIMEOUT = 2;

	/**
	 * http 请求超时
	 */
	public static final int NE_REQUEST_TIMEOUT = 3;

	/**
	 * http 请求失去连接
	 */
	public static final int NE_REQUEST_LOST_CONNECT = 4;
}
