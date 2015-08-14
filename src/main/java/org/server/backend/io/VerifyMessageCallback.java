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
 * 验证消息回调
 *
 * @author Administrator
 * @param <M> 消息模块类型
 * @param <U> 用户模块类型
 */
@FunctionalInterface
public interface VerifyMessageCallback<M extends GeneratedMessage, U> {

    void callback(GameSession session, SessionMessage message, U userModel, M messageModel);
}
