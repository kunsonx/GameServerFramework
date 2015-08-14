/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.backend.io;

import com.google.protobuf.GeneratedMessage;
import org.server.backend.session.GameSession;
import org.server.core.io.SessionMessage;

/**
 * 传输消息回调接口
 *
 * @author Administrator
 * @param <M> 模块具体类型
 */
@FunctionalInterface
public interface MessageCallback<M extends GeneratedMessage> {

    /**
     * 回调接口
     *
     * @param session 游戏会话
     * @param message 会话消息
     * @param model 数据模块
     */
    void callback(GameSession session, SessionMessage message, M model);
}
