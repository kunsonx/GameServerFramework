package org.server.frontend.jmx;

import java.util.List;
import org.server.frontend.FrontendServer;

/**
 * 前台服务Jmx 实现类
 *
 * @author Hxms
 */
public class Frontend implements FrontendMXBean {

    private FrontendServer server;

    public Frontend(FrontendServer server) {
        this.server = server;
    }

    @Override
    public String getServerName() {
        return server.getServerConfig().getServerKey();
    }

    @Override
    public FrontendConnectTable[] getConnectTable() {
        return server.getServerConnectTable().toArray(new FrontendConnectTable[0]);
    }
}
