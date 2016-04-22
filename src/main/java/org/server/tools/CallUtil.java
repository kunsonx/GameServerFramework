package org.server.tools;

import java.util.function.Consumer;

import com.google.common.base.Function;

public class CallUtil {
    
    /**
     * 安全调用任何函数
     * 
     * @param callback
     *            回调接口
     * @return 调用过程是否发生异常
     */
    public static <T> boolean safeCallAny(T object, Consumer<T> callback) {
        return safeCallAny(object, callback, null);
    }
    
    /**
     * 安全调用任何函数
     * 
     * @param callback
     *            回调接口
     * @return 调用过程是否发生异常
     */
    public static <T> boolean safeCallAny(T object, Consumer<T> callback, Consumer<Throwable> throwableHandler) {
        
        /**
         * 空对象直接返回
         */
        if (object == null || callback == null) return false;
        
        try {
            callback.accept(object);
        } catch (Throwable e) {
            safeCallAnyThrowableHandler(e, throwableHandler);
            return false;
        }
        return true;
    }
    
    /**
     * 安全调用任何函数
     * 
     * @param callback
     *            回调接口
     * @return 调用过程是否发生异常
     */
    public static <T, R> R safeCallAnyFunction(T object, Function<T, R> callback,
            Consumer<Throwable> throwableHandler) {
            
        /**
         * 空对象直接返回
         */
        if (object == null || callback == null || throwableHandler == null) return null;
        
        try {
            return callback.apply(object);
        } catch (Throwable e) {
            safeCallAnyThrowableHandler(e, throwableHandler);
            return null;
        }
    }
    
    /**
     * 安全调用接口 - 异常处理
     * 
     * @param e
     *            异常
     * @param throwableHandler
     *            异常处理函数
     */
    static void safeCallAnyThrowableHandler(Throwable e, Consumer<Throwable> throwableHandler) {
        try {
            if (throwableHandler != null) throwableHandler.accept(e);
        } catch (Throwable throwable) {
            safeCallAnyThrowableHandler(throwable, throwableHandler);
        }
    }
}
