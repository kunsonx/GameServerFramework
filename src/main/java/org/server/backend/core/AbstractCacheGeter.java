/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.backend.core;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存读取器
 *
 * @author Hxms
 * @param <T> 用户类型
 */
public abstract class AbstractCacheGeter<T> extends AbstractGeter<T> {

    protected final ConcurrentHashMap<Object, T> _cache = new ConcurrentHashMap<>();
    protected boolean _enable = true;

    @Override
    public synchronized T get(String type, Object key) {
        // 尝试读取缓存
        if (_enable && _cache.containsKey(key)) {
            return _cache.get(key);
        }
        // 底层读取
        T result = super.get(type, key); //To change body of generated methods, choose Tools | Templates.
        return result;
    }

    /**
     * 清理缓存
     */
    public void clearCache() {
        synchronized (this) {
            _cache.clear();
        }
    }

    /**
     * 启用缓存
     */
    public void enableCache() {
        this._enable = true;
    }

    /**
     * 禁用缓存
     */
    public void disabledCache() {
        this._enable = false;
    }

    /**
     * 注册缓存
     *
     * @param result 用户对象
     */
    protected void registerCache(T result) {
        if (!_enable) {
            return;
        }
        try {
            synchronized (this) {
                initialize0(result);
                registerCache0(result);
            }
        } catch (Exception e) {
            _log.error("[AbstractUserCenter] : 尝试注册缓存异常：", e);
        }
    }

    /**
     * 注册缓存
     *
     * @param model 用户对象模块
     */
    protected abstract void registerCache0(T model);

    /**
     * 初始化数据模块
     *
     * @param model 用户数据模块
     */
    protected abstract void initialize0(T model);
}
