/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.backend.component;

import org.server.backend.BackendServer;
import org.server.backend.BackendServerCustomConfig;

/**
 * 游戏组件
 *
 * @author Hxms
 */
public interface GameComponent {

    /**
     * 加载游戏组件
     */
    void load();

    /**
     * 卸载游戏组件
     */
    void unload();

    /**
     * 获得服务自定义配置
     *
     * @return 自定义配置
     */
    default BackendServerCustomConfig getServerCustomConfig() {
        return BackendServer.getInstance().getCustomConfig();
    }
}
