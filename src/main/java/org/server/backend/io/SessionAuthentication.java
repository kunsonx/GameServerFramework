/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.backend.io;

import org.server.backend.session.GameSession;

/**
 * 会话认证接口函数
 *
 * @author Administrator
 */
@FunctionalInterface
public interface SessionAuthentication {

    /**
     * 尝试认证指定会话
     *
     * @param session 会话
     * @return 用户模块
     */
    Object authentication(GameSession session);
}
