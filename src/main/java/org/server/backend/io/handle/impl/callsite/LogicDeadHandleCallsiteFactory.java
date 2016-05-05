package org.server.backend.io.handle.impl.callsite;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.server.backend.component.GameInitializeException;
import org.server.backend.io.handle.impl.exception.TransportException;
import org.server.backend.session.GameSession;
import org.server.core.io.SessionMessage;

import com.google.protobuf.MessageLite;

import javassist.CtMethod;

public class LogicDeadHandleCallsiteFactory extends AbstractLogicCallsiteFactory {
    
    protected LogicDeadHandleCallsiteFactory() {
        super();
    }
    
    public LogicDeadHandleCallsiteFactory(Object target, Class<?> componentClass, Method method,
            Class<?>[] parametersTypes) {
        super(target, componentClass, method, parametersTypes);
    }
    
    @Override
    protected void validateCallbackArgs0(long messageTypeCount, long logicDataModelTypeCount, long gameSessionTypeCount,
            long sessionMessageTypeCount, long unknowTypeCount, Class<?> returnType) {
        if (gameSessionTypeCount != 1) throw new GameInitializeException(
                String.format("死消息函数配置错误，GameSession 参数必须有一个. :: %s.%s", cClass.toString(), cMethod.toString()));
        if (sessionMessageTypeCount > 1) throw new GameInitializeException(
                String.format("死消息函数配置错误，SessionMessage 参数大于一个. :: %s.%s", cClass.toString(), cMethod.toString()));
        if (messageTypeCount > 1) throw new GameInitializeException(
                String.format("死消息函数配置错误，MessageLite 参数大于一个. :: %s.%s", cClass.toString(), cMethod.toString()));
        else if (Stream.of(parametersTypes)
                .filter(x -> MessageLite.class.isAssignableFrom(x) && !MessageLite.class.equals(x)).findAny()
                .isPresent())
            throw new GameInitializeException(
                    String.format("死消息函数配置错误，消息类型参数只能为  MessageLite. :: %s.%s", cClass.toString(), cMethod.toString()));
        if (logicDataModelTypeCount != 0) throw new GameInitializeException(
                String.format("认证函数配置错误，ILogicDataModel 类型参数不能出现. :: %s.%s", cClass.toString(), cMethod.toString()));
                
        for (Class<?> pt : parametersTypes) {
            if (TransportException.class.isAssignableFrom(pt)) {
                if (!TransportException.class.equals(pt))
                    throw new GameInitializeException(String.format("死消息函数配置错误，异常类型参数只能指定 TransportException. :: %s.%s",
                            cClass.toString(), cMethod.toString()));
                else unknowTypeCount--;
                break;
            }
        }
        
        if (unknowTypeCount != 0) throw new GameInitializeException(
                String.format("认证函数配置错误，存在不识别的参数. :: %s.%s", cClass.toString(), cMethod.toString()));
    }
    
    public LogicDeadHandleCallsite buildCallsite() {
        Map<Class<?>, String> params = new LinkedHashMap<>();
        CallMethodCodeMake methodCodeMake = new CallMethodCodeMake();
        
        params.put(GameSession.class, "session");
        params.put(SessionMessage.class, "message");
        params.put(MessageLite.class, "messageLite");
        params.put(TransportException.class, "transportException");
        
        methodCodeMake.makePublic();
        methodCodeMake.makeMethodSign("invoke", Void.class, params);
        methodCodeMake.makeCodeBlockBegin();
        methodCodeMake.makeDirectInvoke(cClass, cMethod, params);
        methodCodeMake.makeCodeBlockEnd();
        
        return (LogicDeadHandleCallsite) buildCallsite0((p, x) -> {
            x.addInterface(p.get(LogicDeadHandleCallsite.class.getName()));
            x.addMethod(CtMethod.make(methodCodeMake.toString(), x));
        } , context);
    }
}
