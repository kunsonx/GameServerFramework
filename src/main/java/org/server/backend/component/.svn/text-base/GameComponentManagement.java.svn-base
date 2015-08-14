/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.backend.component;

import java.util.HashMap;
import java.util.Map;
import org.server.tools.ClassFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 游戏组件管理器
 *
 * @author Hxms
 */
public class GameComponentManagement {

    private static final Logger log = LoggerFactory.getLogger(GameComponentManagement.class);
    /**
     * 加载组件包名称
     */
    private static final String packageName = "org.server.component";
    private static final Map<String, GameComponent> _components = new HashMap<>();

    /**
     * 载入所有组件
     */
    public static void loadAllComponent() {
        try {
            ClassFinder classFinder = new ClassFinder();
            String[] classes = classFinder.listClasses(packageName, true);
            for (String clazz : classes) {
                Class<?> clasz = Class.forName(clazz);
                if (GameComponent.class.isAssignableFrom(clasz)) {
                    try {
                        GameComponent newInstance = (GameComponent) clasz.newInstance();
                        registerComponent(clazz, newInstance);
                    } catch (InstantiationException | IllegalAccessException e) {
                        log.error("ERROR INSTANCIATING GameComponent CLASS", e);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            log.error("THROW", e);
        }
        // 运行所有组件
        startAllComponent();
    }

    /**
     * 卸载所有组件
     */
    public static void unloadAllComponent() {
        _components.values().forEach(s -> {
            String cName = s.getClass().getName();
            try {
                s.unload();
                log.info(String.format("[%s] Component Unload.", cName));
            } catch (Exception e) {
                log.error(String.format("尝试卸载组件 [%s] 失败：", cName), e);
            }
        });
        _components.clear();
    }

    /**
     * 注册组件
     *
     * @param clazz 组件类名
     * @param newInstance 组件实例
     */
    private static void registerComponent(String clazz, GameComponent newInstance) {
        _components.put(clazz, newInstance);
    }

    /**
     * 开始所有组件
     */
    private static void startAllComponent() {
        _components.values().forEach(s -> {
            String cName = s.getClass().getName();
            try {
                s.load();
                log.info(String.format("[%s] Component Load.", cName));
            } catch (Exception e) {
                log.error(String.format("尝试启动组件 [%s] 失败：", cName), e);
            }
        });
    }
}
