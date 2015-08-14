/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.core.remote;

import java.rmi.RemoteException;

/**
 * 远程对象操作接口
 * @param <T> 远程接口类型F
 */
@FunctionalInterface
public interface RemoteObjectAction<T> {

    void run(T obj) throws RemoteException;
}
