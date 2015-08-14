/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.master.remote;

import java.rmi.RemoteException;
import org.server.core.IPAddress;
import org.server.core.remote.RMIServiceInterface;

/**
 *
 * @author Administrator
 */
public interface MasterRMIServerInterface extends RMIServiceInterface {

    /**
     * 注册服务
     *
     * @param key 关键字
     * @param service 服务类型
     * @return
     * @throws RemoteException
     */
    public boolean registerServer(String key, RMIServiceInterface service) throws RemoteException;

    /**
     * 获得RMI服务从服务钥匙检索
     *
     * @param key
     * @return
     * @throws java.rmi.RemoteException
     */
    public IPAddress getRMIAddressByKey(String key)throws RemoteException;
}
