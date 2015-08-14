/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.backend.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.server.backend.BackendServer;

/**
 * 游戏会话提供程序
 *
 * @author Administrator
 */
public class GameSessionProvider {

    private final Map<BackendSession, GameSession> _sessions = new ConcurrentHashMap<>();

    /**
     * 读取游戏会话对象
     *
     * @param session 后台服务会话标识
     * @return 游戏会话对象
     */
    public GameSession getGameSession(BackendSession session) {
        GameSession result = _sessions.get(session);
        if (result == null) {
            // 创建会话
            result = new GameSession(session);
            result.setAttribute(GameSession.SESSION_CREATE, System.currentTimeMillis());
            result.setAttribute(GameSession.SEEION_ADDRESS, BackendServer.getInstance().getBackendRMIServerInterface().getAddress(session));
            // 放入缓存
            _sessions.put(session, result);
        }
        return result;
    }

    /**
     * 销毁游戏会话对象
     *
     * @param session 后台服务会话标识
     */
    public void dispose(BackendSession session) {
        GameSession result = _sessions.get(session);
        if (result != null) {
            result.setDispose(true);
            // 删除引用
            _sessions.remove(session);
        }
    }

    public Map<BackendSession, GameSession> getSessions() {
        return _sessions;
    }

    private static final GameSessionProvider _instance = new GameSessionProvider();

    /**
     * 获得游戏会话对象实例
     *
     * @return 管理器实例
     */
    public static GameSessionProvider getInstance() {
        return _instance;
    }
}
