/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.command;

/**
 * 本地命令回调函数
 *
 * @author Hxms
 */
@FunctionalInterface
public interface LocalCommandCallback {

    /**
     * 回调接口
     */
    void callback();
}
