package org.server.core.netty.http;

public class NHttpResponse {
    
    /**
     * 请求响应
     */
    String response;
           
    /**
     * 表示请求最后回应
     */
    String content   = null;
                     
    /**
     * 表示请求最后响应代码
     */
    int    code      = 0;
                     
    /**
     * 表示请求最后响应错误代码
     */
    int    errorCode = 0;
                     
    public NHttpResponse(String response, String content, int code, int errorCode) {
        super();
        this.response = response;
        this.content = content;
        this.code = code;
        this.errorCode = errorCode;
    }
    
    /**
     * @return the response
     */
    public String response() {
        return response;
    }
    
    /**
     * @return the content
     */
    public String content() {
        return content;
    }
    
    /**
     * @return the code
     */
    public int code() {
        return code;
    }
    
    /**
     * @return the errorCode
     */
    public int errorCode() {
        return errorCode;
    }
}
