/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.core.hibernate;

import java.util.HashMap;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 模块自动记录器
 *
 * @author Hxms
 */
public class OrmAutoLogger {

    static final Logger log = LoggerFactory.getLogger(OrmAutoLogger.class);
    static HashMap<Class, Class> _proxyClasses = new HashMap<>();// 被代理类型对应代理类型
    static HashMap<String, String> _proxyNameMap = new HashMap<>();// 代理类型对应被代理类型

    /**
     * 添加自动代理类型
     *
     * @param <T> 代理类型
     * @param cls 代理类型 Class 对象
     * @return 注册是否成功
     */
     static <T> boolean addAutoProxy(Class<T> cls) {
        try {
            CtClass ctClass = ClassPool.getDefault().get(cls.getName());// 读取要被代理的类型
            CtClass loggerClass = ClassPool.getDefault().getAndRename(OrmChangeLoggerModel.class.getName(), cls.getName() + "AutoProxy");// 读取通用父类
            loggerClass.setSuperclass(ctClass);// 设置父类
            Class proxyCls = loggerClass.toClass();
            _proxyClasses.put(cls, proxyCls);
            registerProxyClass(proxyCls, cls);
        } catch (CannotCompileException | NotFoundException ex) {
            log.error("Can't Create Proxy Class!", ex);
        }
        return true;
    }

    /**
     * 是否有指定类型的自动代理
     *
     * @param cls 检测代理类型
     * @return 是否有代理
     */
    public static boolean hasAutoProxy(Class cls) {
        return _proxyClasses.containsKey(cls);
    }

    /**
     * 实例化代理对象
     *
     * @param cls 注册类型代理
     * @return 代理对象
     */
    public static Object instantiate(Class cls) {
        try {
            if (_proxyClasses.containsKey(cls)) {
                Object result = _proxyClasses.get(cls).newInstance();
                if (result instanceof OrmChangeLoggerModelInterface) {// 是否实现自动字段记录器
                    registerProxyClass(((OrmChangeLoggerModelInterface) result).proxy().getClass(), cls);
                }
                return result;
            }
        } catch (InstantiationException | IllegalAccessException ex) {
            log.error("Can't Create Proxy Object!", ex);
        }
        return null;
    }

    /**
     * 获得实体原始名称
     *
     * @param cls 代理类型对象
     * @return 类名称
     */
    public static String getEntityName(Class cls) {
        if (_proxyNameMap.containsKey(cls.getName())) {
            return _proxyNameMap.get(cls.getName());
        }
        return null;
    }

    public static void registerProxyClass(Class proxyCls, Class cls) {
        _proxyNameMap.put(proxyCls.getName(), cls.getName());
    }
}
