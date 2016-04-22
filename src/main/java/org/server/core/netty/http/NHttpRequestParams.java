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

import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.ClientCookieEncoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder.ErrorDataEncoderException;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.server.tools.SystemUtil;

/**
 * 
 * netty http 请求组装器
 * 
 * @author Hxms
 *         
 */
public class NHttpRequestParams {
    
    // Force to use memory-based data.
    final static DefaultHttpDataFactory inMemoryFactory = new DefaultHttpDataFactory(false);
    URI                                 uri;
    HttpMethod                          method          = HttpMethod.GET;
    HttpVersion                         version         = HttpVersion.HTTP_1_1;
    String                              userAgent       = "Dalvik/1.6.0 (Linux; U)";
    Map<String, String>                 form, headers;
    Map<String, File>                   uploadFormFile;
    boolean                             multipart;
    Cookie[]                            cookies;
    int                                 connectTimeout  = 3000,
                                                requestTimeout = 10000, retryConnectCount = 3;
    NHttpRequestCallback                callback;
    Charset                             responseCharset = CharsetUtil.UTF_8;
                                                        
    private NHttpRequestParams(URI uri) {
        this.uri = uri;
    }
    
    /**
     * 获得请求的地址
     * 
     * @return 请求地址
     */
    public URI uri() {
        return uri;
    }
    
    /**
     * @return the version
     */
    public HttpVersion version() {
        return version;
    }
    
    /**
     * @param version
     *            the version to set
     */
    public NHttpRequestParams version(HttpVersion version) {
        this.version = version;
        return this;
    }
    
    /**
     * @return the method
     */
    public HttpMethod method() {
        return method;
    }
    
    /**
     * @param method
     *            the method to set
     */
    public NHttpRequestParams method(HttpMethod method) {
        this.method = method;
        return this;
    }
    
    /**
     * 添加请求头
     * 
     * @param name
     *            名称
     * @param value
     *            值
     */
    public void header(String name, String value) {
        if (this.headers == null) headers(new HashMap<String, String>());
        this.headers.put(name, value);
    }
    
    /**
     * 请求是否是 Https 请求
     * 
     * @return 标识请求是否为 SSL 请求
     */
    public boolean https() {
        return uri.getScheme().equalsIgnoreCase("https");
    }
    
    /**
     * 获得请求提交表单
     * 
     * @return the form 表单
     */
    public Map<String, String> form() {
        return form;
    }
    
    /**
     * 设置请求提交的表单
     * 
     * @param form
     *            the form to set 要在请求提交的表单数据
     */
    public NHttpRequestParams form(Map<String, String> form) {
        this.form = form;
        return this;
    }
    
    /**
     * 添加表单项
     * 
     * @param name
     *            项目名称
     * @param value
     *            项目值
     */
    public void form(String name, String value) {
        if (this.form == null) this.form = new HashMap<String, String>();
        this.form.put(name, value);
    }
    
    /**
     * 添加上传文件
     * 
     * @param name
     *            名称
     * @param value
     *            文件
     */
    public boolean uploadFile(String name, File value) {
        if (!value.exists() || !value.isFile()) return false;
        if (this.uploadFormFile == null) this.uploadFormFile = new HashMap<String, File>();
        this.uploadFormFile.put(name, value);
        return true;
    }
    
    /**
     * True if the FORM is a ENCTYPE="multipart/form-data"
     * 
     * @return the multipart
     */
    public boolean multipart() {
        return multipart;
    }
    
    /**
     * True if the FORM is a ENCTYPE="multipart/form-data"
     * 
     * @param multipart
     *            the multipart to set
     */
    public void multipart(boolean multipart) {
        this.multipart = multipart;
    }
    
    /**
     * 获得请求头数据
     * 
     * @return the headers 头数据
     */
    public Map<String, String> headers() {
        return headers;
    }
    
    /**
     * 设置请求头数据
     * 
     * @param headers
     *            the headers to set 请求使用的头数据
     */
    public NHttpRequestParams headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }
    
    /**
     * 获得请求使用的 Cookie
     * 
     * @return the cookies
     */
    public Cookie[] cookies() {
        return cookies;
    }
    
    /**
     * 设置请求使用的 Cookie
     * 
     * @param cookies
     *            the cookies to set 该请求使用的 Cookie
     */
    public NHttpRequestParams cookies(Cookie[] cookies) {
        this.cookies = cookies;
        return this;
    }
    
    /**
     * 
     * 获得请求回调函数
     * 
     * @return the callback
     */
    public NHttpRequestCallback callback() {
        return callback;
    }
    
    /**
     * 
     * 设置请求回调
     * 
     * @param callback
     *            the callback to set
     */
    public NHttpRequestParams callback(NHttpRequestCallback callback) {
        this.callback = callback;
        return this;
    }
    
    /**
     * 
     * 获得 socket 连接超时时间
     * 
     * @return the connectTimeout
     */
    public int connectTimeout() {
        return connectTimeout;
    }
    
    /**
     * 
     * 设置连接超时时间
     * 
     * @param connectTimeout
     *            the connectTimeout to set
     */
    public NHttpRequestParams connectTimeout(int connectTimeout) {
        this.connectTimeout = Math.max(connectTimeout, 1);
        return this;
    }
    
    /**
     * 
     * 获得请求超时时间
     * 
     * @return the requestTimeout
     */
    public int requestTimeout() {
        return requestTimeout;
    }
    
    /**
     * 
     * 设置请求时间
     * 
     * @param requestTimeout
     *            the requestTimeout to set
     */
    public NHttpRequestParams requestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
        return this;
    }
    
    /**
     * 获得响应解码用字符集
     * 
     * @return the responseCharset
     */
    public Charset responseCharset() {
        return responseCharset;
    }
    
    /**
     * 设置响应字符串
     * 
     * @param responseCharset
     *            the responseCharset to set 解码响应所用字符集
     */
    public NHttpRequestParams responseCharset(Charset responseCharset) {
        this.responseCharset = responseCharset;
        return this;
    }
    
    /**
     * 
     * 获得 user agent 头
     * 
     * @return the userAgent
     */
    public String userAgent() {
        return userAgent;
    }
    
    /**
     * 
     * 设置 user agent 头
     * 
     * @param userAgent
     *            the userAgent to set
     */
    public NHttpRequestParams userAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }
    
    /**
     * 
     * 获得重试连接次数
     * 
     * @return the retryConnectCount
     */
    public int retryConnectCount() {
        return retryConnectCount;
    }
    
    /**
     * 
     * 设置重试连接次数
     * 
     * @param retryConnectCount
     *            the retryConnectCount to set
     */
    public NHttpRequestParams retryConnectCount(int retryConnectCount) {
        this.retryConnectCount = Math.max(retryConnectCount, 0);
        return this;
    }
    
    /**
     * 获得链接的地址
     * 
     * @return 地址
     */
    protected String hostname() {
        return uri.getHost();
    }
    
    /**
     * 获得端口
     * 
     * @return 端口
     */
    protected int port() {
        return uri.getPort() == -1 ? https() ? 443 : 80 : uri.getPort();
    }
    
    /**
     * 获得表单对象
     * 
     * @return
     * @throws ErrorDataEncoderException
     *             无法编译 post 数据
     */
    Object[] getFormObject(HttpRequest request) throws ErrorDataEncoderException {
        boolean formNoneEntity = form == null || form.isEmpty();
        boolean uploadFileNoneEntity = uploadFormFile == null || uploadFormFile.isEmpty();
        if (formNoneEntity && uploadFileNoneEntity) return null;
        HttpPostRequestEncoder encoder = new HttpPostRequestEncoder(inMemoryFactory, request, multipart);
        if (!formNoneEntity) {
            for (Entry<String, String> item : form.entrySet()) {
                encoder.addBodyAttribute(item.getKey(), item.getValue());
            }
        }
        if (!uploadFileNoneEntity) {
            for (Entry<String, File> item : uploadFormFile.entrySet()) {
                encoder.addBodyFileUpload(item.getKey(), item.getValue(),
                        headers != null ? headers.get(HttpHeaders.Names.CONTENT_TYPE) : null, false);
            }
        }
        return new Object[] { encoder.finalizeRequest(), encoder };
    }
    
    /**
     * 获得 http 请求内容
     * 
     * @return 请求内容
     * @throws ErrorDataEncoderException
     *             无法编译 post 数据
     */
    protected Object[] buildRequest() throws ErrorDataEncoderException {
        // handler the uri
        String requestUri = null;
        String path = uri.getRawPath();
        String query = uri.getRawQuery();
        URI uri = this.uri;
        // concat uri
        if (query == null || query == "") requestUri = path;
        else requestUri = String.format("%s?%s", path, query);
        // create new path
        uri = URI.create(requestUri);
        // create request
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(version, method, uri.toASCIIString());
        buildRequestHeaders(request);
        // encoder post
        Object[] finalRequest = getFormObject(request);
        // if encoder is null
        if (finalRequest == null) {
            return new HttpObject[] { request };
        } else {
            return finalRequest;
        }
    }
    
    private void buildRequestHeaders(DefaultFullHttpRequest request) {
        // config request header
        request.headers().set(HttpHeaders.Names.HOST, this.uri.getHost());
        // 添加压缩头
        request.headers().set(HttpHeaders.Names.ACCEPT_ENCODING, "gzip, deflate");
        // added user agent
        if (userAgent != null && !userAgent.isEmpty()) request.headers().set(HttpHeaders.Names.USER_AGENT, userAgent);
        else request.headers().remove(HttpHeaders.Names.USER_AGENT);
        // add custom
        if (this.headers != null) {
            for (Entry<String, String> item : this.headers.entrySet()) {
                request.headers().set(item.getKey(), item.getValue());
            }
        }
        // add cookie
        if (this.cookies != null) {
            String cookieValue = ClientCookieEncoder.STRICT.encode(cookies);
            request.headers().set(HttpHeaders.Names.COOKIE, cookieValue);
        }
    }
    
    /**
     * 创建请求配置
     * 
     * @param uri
     *            地址
     * @return 配置
     */
    public static NHttpRequestParams create(URI uri) {
        return new NHttpRequestParams(uri);
    }
    
    /**
     * 创建请求配置
     * 
     * @param uri
     *            地址
     * @return 配置
     */
    public static NHttpRequestParams create(String uri) {
        return new NHttpRequestParams(URI.create(uri));
    }
    
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Object[] httpObjects = buildRequest();
            for (int i = 0; i < httpObjects.length; i++)
                if (httpObjects[i] != null)
                    stringBuilder.append(httpObjects[i].toString()).append(SystemUtil.LINE_SEPARATOR);
        } catch (Throwable e) {
        }
        return stringBuilder.toString();
    }
}
