 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.backend;

import org.server.core.AbstractServerConfig;
import org.w3c.dom.Document;

/**
 * 后台服务配置
 *
 * @author Administrator
 */
public class BackendServerConfig extends AbstractServerConfig {

    // 后台自定义配置项
    BackendServerCustomConfig _customConfig;

    public BackendServerConfig() {
        super("config/backend.xml");
    }

    @Override
    public void readConfig(Document document) {
        _customConfig = new BackendServerCustomConfig(document);
    }

    public BackendServerCustomConfig getCustomConfig() {
        return _customConfig;
    }
}
