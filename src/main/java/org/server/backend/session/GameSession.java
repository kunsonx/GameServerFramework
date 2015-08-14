/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.backend.session;

import com.google.protobuf.GeneratedMessage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.server.backend.io.Transport;

/**
 * 游戏会话
 *
 * @author Administrator
 */
public class GameSession implements java.io.Serializable {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5795348889101186868L;
	public static final String SESSION_CREATE = "SESSION_CREATE";
    public static final String SEEION_ADDRESS = "SEEION_ADDRESS";

    final BackendSession _backendSession;
    final Map<String, Object> _attribute = new ConcurrentHashMap<>();
    boolean _dispose = false;

    // 统计数据
    volatile int _inputBytes = 0;
    volatile int _outputBytes = 0;

    /**
     * 构造一个新的游戏会话对象
     *
     * @param _backendSession 后台服务会话
     */
    public GameSession(BackendSession _backendSession) {
        this._backendSession = _backendSession;
    }

    /**
     * 获得后台会话标识对象
     *
     * @return
     */
    public BackendSession getBackendSession() {
        return _backendSession;
    }

    public int getInputBytes() {
        return _inputBytes;
    }

    public void setInputBytes(int _inputBytes) {
        this._inputBytes = _inputBytes;
    }

    public int getOutputBytes() {
        return _outputBytes;
    }

    public void setOutputBytes(int _outputBytes) {
        this._outputBytes = _outputBytes;
    }

    /**
     * 获得属性
     *
     * @param key 键值
     * @return 返回值
     */
    public Object getAttribute(String key) {
        return _attribute.get(key);
    }

    /**
     * 设置属性
     *
     * @param key 键
     * @param value 值
     */
    public void setAttribute(String key, Object value) {
        _attribute.put(key, value);
    }

    /**
     * 移除属性
     *
     * @param key 键
     * @return 值
     */
    public Object removeAttribute(String key) {
        return _attribute.remove(key);
    }

    /**
     * 获得会话是否注销 - [前台 Socket 事件注销]
     *
     * @return
     */
    public boolean isDispose() {
        return _dispose;
    }

    public void setDispose(boolean _dispose) {
        this._dispose = _dispose;
    }

    /**
     * 写入消息
     *
     * @param obj 消息对象
     */
    public void write(GeneratedMessage obj) {
        Transport.write(this, obj);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String line = System.getProperty("line.separator");

        sb.append("会话前台编号：")
                .append(getBackendSession())
                .append(line)
                .append("输入流量：")
                .append(getInputBytes() / 1000.0)
                .append(" KB.")
                .append(line)
                .append("输出流量：")
                .append(getOutputBytes() / 1000.0)
                .append(" KB.");

        return sb.toString(); //To change body of generated methods, choose Tools | Templates.
    }
}
