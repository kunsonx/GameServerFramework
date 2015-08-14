/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.core.remote;

import java.rmi.RemoteException;

/**
 * 支持关机服务接口
 *
 * @author Administrator
 */
public interface ShutdownServiceInterface {

    /**
     * 关闭服务器
     *
     * @return 关闭是否成功
     * @throws RemoteException
     */
    public boolean shutdown() throws RemoteException;
}
