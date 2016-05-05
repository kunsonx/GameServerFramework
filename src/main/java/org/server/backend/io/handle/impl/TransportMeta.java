package org.server.backend.io.handle.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.server.backend.component.GameComponent;
import org.server.backend.component.GameInitializeException;
import org.server.backend.io.TransportV2;
import org.server.backend.io.handle.annotation.LogicAuthorize;
import org.server.backend.io.handle.annotation.LogicDeadMessage;
import org.server.backend.io.handle.annotation.LogicHandle;
import org.server.backend.io.handle.annotation.LogicModelMeta;
import org.server.backend.io.handle.annotation.LogicModelMetas;
import org.server.backend.io.handle.impl.callsite.LogicHandleCallsiteFactory;
import org.server.backend.io.handle.impl.callsite.LogicAuthorizeCallsite;
import org.server.backend.io.handle.impl.callsite.LogicAuthorizeCallsiteFactory;
import org.server.backend.io.handle.impl.callsite.LogicDeadHandleCallsite;
import org.server.backend.io.handle.impl.callsite.LogicDeadHandleCallsiteFactory;
import org.server.backend.io.handle.impl.callsite.LogicHandleCallsite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransportMeta {
    
    static final Logger                                log                                  = LoggerFactory
            .getLogger(TransportMeta.class);
            
    public static List<LogicModelMeta>                 MODEL_META                           = new ArrayList<>();
    public static List<LogicHandleCallsite>            CALLBACK_META                        = new ArrayList<>();
    public static List<LogicAuthorizeCallsite>         AUTHORIZE_META                       = new ArrayList<>();
    public static List<LogicDeadHandleCallsite>        DEAD_HANDLE_META                     = new ArrayList<>();
    public static Map<Integer, LogicModelMeta>         MODEL_META_TABLE                     = new HashMap<>();
    public static Map<Integer, LogicModelMeta>         MODEL_IN_META_TABLE                  = new HashMap<>();
    public static Map<Class<?>, LogicModelMeta>         MODEL_OUT_META_TABLE                 = new HashMap<>();
    public static Map<Integer, LogicHandleCallsite>    CALLBACK_WITH_MESSAGETYPE_META_TABLE = new HashMap<>();
    public static Map<Integer, LogicAuthorizeCallsite> AUTHORIZE_META_TABLE                 = new HashMap<>();
                                                                                            
    public static LogicAuthorizeCallsite findAuthorizeWithReturnType(Class<?> returnType) {
        return returnType != null ? AUTHORIZE_META_TABLE.get(returnType.hashCode()) : null;
    }
    
    public static LogicHandleCallsite findHandleCallsiteWithAcceptMessageType(Class<?> messageType) {
        return messageType != null ? CALLBACK_WITH_MESSAGETYPE_META_TABLE.get(messageType.hashCode()) : null;
    }
    
    public static void scannerLogicConfig(LinkedList<GameComponent> loadedComponents) {
        for (GameComponent gameComponent : loadedComponents) {
            Class<?> componentClass = gameComponent.getClass();
            LogicModelMetas modelMetas = componentClass.getAnnotation(LogicModelMetas.class);
            Method[] methods = componentClass.getDeclaredMethods();
            
            if (modelMetas != null) {
                for (LogicModelMeta annotation : modelMetas.metas()) {
                    MODEL_META.add(annotation);
                    MODEL_META_TABLE.put(annotation.command(), annotation);
                    if (annotation.commandIsSend()) MODEL_OUT_META_TABLE.put(annotation.owner(), annotation);
                    else MODEL_IN_META_TABLE.put(annotation.command(), annotation);
                }
            }
            
            for (Method method : methods) {
                if (method.getAnnotation(LogicHandle.class) != null) {
                    Class<?> parametersTypes[] = method.getParameterTypes();
                    LogicHandleCallsiteFactory callback = new LogicHandleCallsiteFactory(gameComponent, componentClass,
                            method, parametersTypes);
                    callback.validateCallbackArgs();
                    LogicHandleCallsite callsite = callback.buildCallsite();
                    CALLBACK_META.add(callsite);
                    CALLBACK_WITH_MESSAGETYPE_META_TABLE.put(callsite.getAcceptMessageType().hashCode(), callsite);
                } else if (method.getAnnotation(LogicAuthorize.class) != null) {
                    Class<?> parametersTypes[] = method.getParameterTypes();
                    LogicAuthorizeCallsiteFactory factory = new LogicAuthorizeCallsiteFactory(gameComponent,
                            componentClass, method, parametersTypes);
                    factory.validateCallbackArgs();
                    LogicAuthorizeCallsite callsite = factory.buildCallsite();
                    AUTHORIZE_META.add(callsite);
                    AUTHORIZE_META_TABLE.put(callsite.returnDataModel().hashCode(), callsite);
                } else if (method.getAnnotation(LogicDeadMessage.class) != null) {
                    Class<?> parametersTypes[] = method.getParameterTypes();
                    LogicDeadHandleCallsiteFactory factory = new LogicDeadHandleCallsiteFactory(gameComponent,
                            componentClass, method, parametersTypes);
                    factory.validateCallbackArgs();
                    LogicDeadHandleCallsite callsite = factory.buildCallsite();
                    DEAD_HANDLE_META.add(callsite);
                }
            }
        }
        
        for (LogicHandleCallsite site : CALLBACK_META) {
            if (site.getAcceptDataModelType() != null) {
                LogicAuthorizeCallsite authorizeCallsite = findAuthorizeWithReturnType(site.getAcceptDataModelType());
                if (authorizeCallsite == null) throw new GameInitializeException(site.getClass().getName()
                        + " :: can't find datamodel type require " + site.getAcceptDataModelType().getName());
                else log.info("{} => {}", site.getClass().getSimpleName(),
                        authorizeCallsite.getClass().getSimpleName());
            }
        }
        
        TransportV2.initialize();
    }
    
}
