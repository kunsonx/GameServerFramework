/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.core.hibernate;

import java.lang.reflect.Method;
import java.util.List;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 字段修改记录器接口
 *
 * @author Hxms
 */
public interface OrmChangeLoggerModelInterface extends MethodInterceptor {

    /**
     * 添加需要更新的字段
     *
     * @param name 字段名
     */
    void addUpdate(String name);

    /**
     * cglib 拦截器
     *
     * @param proxy 代理对象
     * @param method 代理方法对象
     * @param args 代理参数
     * @param mp 代理方法
     * @return 返回值
     * @throws Throwable 发生的异常
     */
    @Override
    Object intercept(Object proxy, Method method, Object[] args, MethodProxy mp) throws Throwable;

    /**
     * 返回原始模块对象
     *
     * @return
     */
    OrmChangeLoggerModelInterface original();

    /**
     * 获得需要更新的字段名称
     *
     * @return 字段名称集合
     */
    List<String> readNeedUpdateValName();

    /**
     * 获得代理类型
     * @return 返回代理对象
     */
    Object proxy();
}
