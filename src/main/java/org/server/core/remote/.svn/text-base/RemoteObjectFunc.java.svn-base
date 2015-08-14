/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.core.remote;

import java.rmi.RemoteException;

/**
 * 远程对象操作接口
 *
 * @param <T> 远程接口类型F
 * @param <R> 返回参数类型
 */
@FunctionalInterface
public interface RemoteObjectFunc<T, R> {

    R run(T obj) throws RemoteException;
}
