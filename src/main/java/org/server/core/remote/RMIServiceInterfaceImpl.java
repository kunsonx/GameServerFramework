/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.core.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import org.server.core.AbstractStandardServer;
import org.server.core.IPAddress;

public class RMIServiceInterfaceImpl<T extends AbstractStandardServer<?>> extends UnicastRemoteObject implements RMIServiceInterface {

    private final T _server;

    public RMIServiceInterfaceImpl(T server) throws RemoteException {
        _server = server;
    }

    /**
     * 获得服务对象
     *
     * @return
     */
    public T getServer() {
        return _server;
    }

    @Override
    public boolean ping() throws RemoteException {
        return true;
    }

    @Override
    public IPAddress getServerRMIAddress() throws RemoteException {
        return _server.getServerConfig().getRMIAddress();
    }
}
