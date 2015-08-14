/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.core.io;

/**
 * 会话消息模块
 *
 * @author Hxms
 */
public class SessionMessage implements java.io.Serializable {

    private short command;
    private byte[] data;

    public SessionMessage(short command, byte[] data) {
        this.command = command;
        this.data = data;
    }

    public short getCommand() {
        return command;
    }

    public void setCommand(short command) {
        this.command = command;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format("命令值：[0x%s] 消息长度：[%d]", Integer.toHexString(getCommand()), getData().length);
    }
}
