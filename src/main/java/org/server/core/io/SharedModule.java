/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.core.io;

import org.server.core.io.input.LittleEndianAccessor;
import org.server.core.io.output.LittleEndianWriter;

/**
 * 共享模块接口
 *
 * @author Administrator
 */
public interface SharedModule {

    /**
     * 序列化模型
     *
     * @param endianWriter 二进制写入器
     */
    public void serialize(LittleEndianWriter endianWriter);

    /**
     * 反序列化模型
     *
     * @param endianAccessor 二进制读取器
     */
    public void deserialize(LittleEndianAccessor endianAccessor);

}
