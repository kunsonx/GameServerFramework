/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.backend.core;

/**
 * 玩家接口类
 *
 * @author hxms
 * @param <T> 基础模块类型
 */
public interface Player<T> {

    /**
     * 获得基础模块
     *
     * @return 基础模块实例
     */
    T getBasicModel();
}
