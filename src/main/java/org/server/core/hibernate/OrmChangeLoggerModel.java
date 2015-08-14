/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.core.hibernate;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import net.sf.cglib.proxy.*;

/**
 * 字段修改记录器
 *
 * @author Administrator
 */
public class OrmChangeLoggerModel implements OrmChangeLoggerModelInterface {

    final List<String> _needUpdateValName = new LinkedList<>();
    Object _loggerProxy;

    /**
     * 获得需要更新的字段名称
     *
     * @return 字段名称集合
     */
    @Override
    public List<String> readNeedUpdateValName() {
        return _needUpdateValName;
    }

    /**
     * 添加需要更新的字段
     *
     * @param name 字段名
     */
    @Override
    public void addUpdate(String name) {
        synchronized (_needUpdateValName) {
            if (name != null && !_needUpdateValName.contains(name)) {
                _needUpdateValName.add(name);
            }
        }
    }

    /**
     * 返回原始模块对象
     *
     * @return
     */
    @Override
    public OrmChangeLoggerModelInterface original() {
        return this;
    }

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
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy mp) throws Throwable {
        if (method.getName().startsWith("set")) {
            String propName = method.getName().substring(3);
            addUpdate(propName);
        }
        return mp.invoke(this, args);
    }

    /**
     * 获得实体对象自动记录代理类
     *
     * @return 返回自动记录代理对象
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object proxy() {
        if (_loggerProxy == null) {
            Enhancer enhancer = new Enhancer();
            enhancer.setUseCache(true);
            enhancer.setUseFactory(true);
            enhancer.setSuperclass(getClass());// 设置继承类
            enhancer.setCallback(this);// 回调方法  
            _loggerProxy = enhancer.create();
        }
        return _loggerProxy;
    }
}
