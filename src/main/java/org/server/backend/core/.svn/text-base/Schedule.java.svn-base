/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.backend.core;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 计划调度类
 *
 * @author hxms
 */
public class Schedule {

    static final Logger log = LoggerFactory.getLogger(Schedule.class);

    static ScheduledThreadPoolExecutor _scheduledThreadPoolExecutor
            = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());

    /**
     * 固定时间调度回调
     *
     * @param callback 要执行的函数
     * @param reference 用于参考的时间
     * @param duration 基础参考时间的间隔调用时间
     * @return 任务对象
     */
    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable callback, LocalDateTime reference, Duration duration) {
        LocalDateTime now = LocalDateTime.now();// 当前时间
        while (reference.isBefore(now)) {// 引用时间在现在时间之前
            reference = reference.plus(duration);// 累加间隔时间
        }
        return _scheduledThreadPoolExecutor.scheduleAtFixedRate(
                callback,
                Duration.between(now, reference).toMillis(),
                duration.toMillis(),
                TimeUnit.MILLISECONDS);
    }
}
