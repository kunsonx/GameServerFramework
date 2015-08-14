/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.backend.core;

/**
 * 玩家帮助接口
 *
 * @author Hxms
 * @param <?>基础模块类型
 * @param <P>玩家模块类型
 */
public interface PlayerHelperInterface<P extends Player<?>> {

    /**
     * 获得玩家
     *
     * @return 玩家实例
     */
    P getPlayer();
}
