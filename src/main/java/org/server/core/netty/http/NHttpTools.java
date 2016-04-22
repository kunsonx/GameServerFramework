package org.server.core.netty.http;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.cookie.ClientCookieDecoder;
import io.netty.handler.codec.http.cookie.Cookie;

public class NHttpTools {
    
    static final Pattern httpParseHeader = Pattern.compile("\\n(\\S+): (.+)");
    
    /**
     * 获得相应内容头
     * 
     * @param httpHeaderText
     *            响应内容
     * @return 所有返回头
     */
    public static String[][] parseHeaders(String httpHeaderText) {
        LinkedList<String[]> matchResult = new LinkedList<String[]>();
        Matcher matcher = httpParseHeader.matcher(httpHeaderText);
        while (matcher.find()) {
            String[] nameValues = new String[] { matcher.group(1), matcher.group(2) };
            matchResult.add(nameValues);
        }
        return matchResult.toArray(new String[0][]);
    }
    
    /**
     * 读取相应头值
     * 
     * @param response
     *            响应正文
     * @param headerName
     *            头名称
     * @return 对应值
     */
    public static String readHeader(String response, String headerName) {
        for (String[] strings : parseHeaders(response))
            if (strings[0].equalsIgnoreCase(headerName)) return strings[1];
        return "";
    }
    
    /**
     * 获得响应 cookie
     * 
     * @return cookies
     */
    public static Cookie[] getResponseCookies(NHttpResponse response) {
        ArrayList<Cookie> cookies = new ArrayList<Cookie>();
        ClientCookieDecoder clientCookieDecoder = ClientCookieDecoder.STRICT;
        String[][] matchResult = NHttpTools.parseHeaders(response.response());
        for (String[] strings : matchResult) {
            if (strings[0].equalsIgnoreCase(HttpHeaders.Names.SET_COOKIE)) {
                Cookie cookie = clientCookieDecoder.decode(strings[1]);
                cookies.add(cookie);
            }
        }
        return cookies.toArray(new Cookie[0]);
    }
}
