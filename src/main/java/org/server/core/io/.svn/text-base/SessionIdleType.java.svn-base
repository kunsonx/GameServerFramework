/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.core.io;

import org.apache.mina.core.session.IdleStatus;

/**
 * 会话空闲类型
 *
 * @author Administrator
 */
public enum SessionIdleType implements java.io.Serializable {

    READER_IDLE,
    WRITER_IDLE,
    BOTH_IDLE;

    /**
     * 转换会话空闲状态类型
     *
     * @param status Mina 会话空闲状态类型
     * @return
     */
    public static SessionIdleType parse(IdleStatus status) {
        if (IdleStatus.BOTH_IDLE == status) {
            return BOTH_IDLE;
        } else if (IdleStatus.READER_IDLE == status) {
            return READER_IDLE;
        } else if (IdleStatus.WRITER_IDLE == status) {
            return WRITER_IDLE;
        }
        return null;
    }
}
