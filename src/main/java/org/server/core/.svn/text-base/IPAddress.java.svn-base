/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.core;

/**
 * Ip地址类型
 *
 * @author Administrator
 */
public class IPAddress implements java.io.Serializable {

    String _ip;
    int _port;

    public String getIp() {
        return _ip;
    }

    public void setIp(String _ip) {
        this._ip = _ip;
    }

    public int getPort() {
        return _port;
    }

    public void setPort(int _port) {
        this._port = _port;
    }

    /**
     * 验证是否有效
     *
     * @return
     */
    public boolean isValid() {
        return _ip != null && _ip.length() > 0 && _port > 0;
    }
}
