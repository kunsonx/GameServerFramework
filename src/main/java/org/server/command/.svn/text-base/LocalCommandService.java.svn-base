/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.command;

import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 本地命令服务
 *
 * @author Hxms
 */
public class LocalCommandService {

    // 日志记录器
    static final Logger logger = LoggerFactory.getLogger(LocalCommandService.class);
    static LocalCommandService _instance;

    ConcurrentHashMap<String, LocalCommandCallback> _commands = new ConcurrentHashMap<>();

    /**
     * 获得服务实例
     *
     * @return 服务实例
     */
    public static LocalCommandService getInstance() {
        if (_instance == null) {
            _instance = new LocalCommandService();
        }
        return _instance;
    }

    /**
     * 注册命令
     *
     * @param command 命令字符串
     * @param consumer 回调函数
     */
    public void register(String command, LocalCommandCallback consumer) {
        _commands.put(command.toLowerCase(), consumer);
    }

    /**
     * 执行命令
     *
     * @param input 输入字符串
     * @return 执行是否成功
     */
    public boolean execute(String input) {
        LocalCommandCallback callback = getCommand(input);
        if (callback != null) {
            logger.info(String.format("ExecuteCommand[%s]......", input));
            try {
                callback.callback();
            } catch (Exception e) {
                logger.error("LocalCommandService[execute] 执行回调命令错误：", e);
            }
            return true;
        } else {
            logger.error(String.format("Can't Not Find Command [%s]?!", input));
        }
        return false;
    }

    /**
     * 获得命令
     *
     * @param command 命令字符串
     * @return 回调函数对象
     */
    LocalCommandCallback getCommand(String command) {
        return (LocalCommandCallback) _commands.get(command.toLowerCase());
    }
}
