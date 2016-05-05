package org.server.backend.io.handle.impl.callsite;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

import org.server.backend.component.GameInitializeException;
import org.server.backend.io.handle.ILogicDataModel;
import org.server.backend.session.GameSession;
import org.server.core.io.SessionMessage;

import com.google.protobuf.MessageLite;

import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;

public class LogicAuthorizeCallsiteFactory extends AbstractLogicCallsiteFactory {
    
    Class<?> returnType;
    
    protected LogicAuthorizeCallsiteFactory() {
        super();
    }
    
    public LogicAuthorizeCallsiteFactory(Object target, Class<?> componentClass, Method method,
            Class<?>[] parametersTypes) {
        super(target, componentClass, method, parametersTypes);
    }
    
    @Override
    protected void validateCallbackArgs0(long messageTypeCount, long logicDataModelTypeCount, long gameSessionTypeCount,
            long sessionMessageTypeCount, long unknowTypeCount, Class<?> returnType) {
        if (!ILogicDataModel.class.isAssignableFrom(returnType)) throw new GameInitializeException(
                String.format("认证函数配置错误，返回类型必须继承 ILogicDataModel :: %s.%s", cClass.toString(), cMethod.toString()));
        if (gameSessionTypeCount != 1) throw new GameInitializeException(
                String.format("认证函数配置错误，GameSession 参数必须有一个. :: %s.%s", cClass.toString(), cMethod.toString()));
        if (sessionMessageTypeCount > 1) throw new GameInitializeException(
                String.format("认证函数配置错误，SessionMessage 参数大于一个. :: %s.%s", cClass.toString(), cMethod.toString()));
        if (messageTypeCount > 1) throw new GameInitializeException(
                String.format("认证函数配置错误，MessageLite 参数大于一个. :: %s.%s", cClass.toString(), cMethod.toString()));
        else if (Stream.of(parametersTypes)
                .filter(x -> MessageLite.class.isAssignableFrom(x) && !MessageLite.class.equals(x)).findAny()
                .isPresent())
            throw new GameInitializeException(
                    String.format("认证函数配置错误，只能定义消息参数为 MessageLite. :: %s.%s", cClass.toString(), cMethod.toString()));
        if (logicDataModelTypeCount != 0) throw new GameInitializeException(
                String.format("认证函数配置错误，ILogicDataModel 类型参数不能出现. :: %s.%s", cClass.toString(), cMethod.toString()));
        if (unknowTypeCount != 0) throw new GameInitializeException(
                String.format("认证函数配置错误，存在不识别的参数. :: %s.%s", cClass.toString(), cMethod.toString()));
        this.returnType = returnType;
    }
    
    public LogicAuthorizeCallsite buildCallsite() {
        StringBuilder methodDefine = new StringBuilder();
        methodDefine.append("public ").append(ILogicDataModel.class.getName()).append(" invoke(")
                .append(GameSession.class.getName()).append(" session").append(",")
                .append(SessionMessage.class.getName()).append(" message").append(",")
                .append(MessageLite.class.getName()).append(" messageLite").append(")").append("{");
                
        methodDefine.append("return ");
        if ((cMethod.getModifiers() & Modifier.STATIC) == 0)
            methodDefine.append("this.target.").append(cMethod.getName());
        else methodDefine.append(cClass.getName()).append(cMethod.getName());
        methodDefine.append("(");
        for (Class<?> pt : parametersTypes) {
            if (GameSession.class.equals(pt)) methodDefine.append("session").append(",");
            else if (SessionMessage.class.equals(pt)) methodDefine.append("message").append(",");
            else if (MessageLite.class.equals(pt)) methodDefine.append("messageLite").append(",");
            else throw new GameInitializeException("buildCallsite error , unknow type " + pt);
        }
        methodDefine.delete(methodDefine.length() - 1, methodDefine.length());
        methodDefine.append(");");
        methodDefine.append("}");
        
        StringBuilder constructorDefine = new StringBuilder("public ").append(getClassSimpleName()).append("(")
                .append(cClass.getName()).append(" target").append(", ").append("Class returnType").append(")")
                .append("{").append("this(target);").append("this.returnType=returnType;").append("}");
                
        return (LogicAuthorizeCallsite) buildCallsite0((p, x) -> {
            x.addInterface(p.get(LogicAuthorizeCallsite.class.getName()));
            x.addField(CtField.make("Class returnType;", x));
            x.addMethod(CtMethod.make("public Class returnDataModel(){return returnType;}", x));
            x.addConstructor(CtNewConstructor.make(constructorDefine.toString(), x));
            x.addMethod(CtMethod.make(methodDefine.toString(), x));
        } , context, returnType);
    }
}
