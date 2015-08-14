/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.backend;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import org.server.backend.component.GameComponentManagement;
import org.server.backend.jmx.Backend;
import org.server.backend.remote.BackendMasterInterfaceImpl;
import org.server.backend.remote.BackendRMIServerInterfaceImpl;
import org.server.core.AbstractRMIServerClient;
import org.server.core.remote.RMIServiceInterfaceImpl;
import org.server.startup.StartupServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 后台业务逻辑服务器
 *
 * @author Administrator
 */
public class BackendServer extends AbstractRMIServerClient<BackendServerConfig> {

    // 日志记录器
    static final Logger logger = LoggerFactory.getLogger(BackendServer.class);
    // 单例
    static BackendServer _instance;
    // 后台服务RMI管理器
    BackendRMIServerInterfaceImpl _backendRMIServerInterface;

    public BackendServer() {
        super(new BackendServerConfig());
        initialize();
    }

    /**
     * 初始化
     */
    private void initialize() {
        _instance = this;
    }

    public static void main(String[] args) {
        StartupServer startupServer = new StartupServer("org.server.backend.BackendServer");
        startupServer.run();
    }

    /**
     * 获得服务程序域唯一实例
     *
     * @return 服务对象
     */
    public static BackendServer getInstance() {
        return _instance;
    }

    @Override
    protected RMIServiceInterfaceImpl createMasterInterfaceImpl() throws RemoteException {
        return new BackendMasterInterfaceImpl(this);
    }

    @Override
    protected UnicastRemoteObject createRemoteObject() throws RemoteException {
        return getBackendRMIServerInterface();
    }

    @Override
    protected String getRemoteObjectName() {
        return "BackendServer";
    }

    @Override
    protected boolean start0() {
        GameComponentManagement.loadAllComponent();
        registerMBean("Backend", new Backend());
        return super.start0(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean stop0() {
        GameComponentManagement.unloadAllComponent();
        return super.stop0(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 获得自定义配置项
     *
     * @return 配置项
     */
    public BackendServerCustomConfig getCustomConfig() {
        return getServerConfig().getCustomConfig();
    }

    /**
     * 获得后台服务RMI管理器
     *
     * @return 后台服务RMI管理器实例
     */
    public BackendRMIServerInterfaceImpl getBackendRMIServerInterface() {
        if (_backendRMIServerInterface == null) {
            try {
                _backendRMIServerInterface = new BackendRMIServerInterfaceImpl(this);
            } catch (RemoteException ex) {
                logger.error("[BackendServer] 创建远程服务对象失败：", ex);
            }
        }
        return _backendRMIServerInterface;
    }
}
