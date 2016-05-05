package org.server.backend.io.handle.impl.callsite;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.server.backend.component.GameInitializeException;
import org.server.backend.io.handle.ILogicDataModel;
import org.server.backend.session.GameSession;
import org.server.core.io.SessionMessage;

import com.google.protobuf.MessageLite;

import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;

public class LogicHandleCallsiteFactory extends AbstractLogicCallsiteFactory {
    
    Class<?> acceptMessageType;
    Class<?> acceptDataModelType;
             
    protected LogicHandleCallsiteFactory() {
        super();
    }
    
    public LogicHandleCallsiteFactory(Object target, Class<?> componentClass, Method method,
            Class<?>[] parametersTypes) {
        super(target, componentClass, method, parametersTypes);
    }
    
    protected void validateCallbackArgs0(long messageTypeCount, long logicDataModelTypeCount, long gameSessionTypeCount,
            long sessionMessageTypeCount, long unknowTypeCount, Class<?> returnType) {
        if (messageTypeCount != 1) {
            throw new GameInitializeException(String.format("逻辑函数配置错误，MessageLite 参数个数不为 1. :: %s.%s %d",
                    cClass.toString(), cMethod.toString(), messageTypeCount));
        } else if (logicDataModelTypeCount > 1) {
            throw new GameInitializeException(String.format("逻辑函数配置错误，ILogicDataModel 参数个数大于 1. :: %s.%s %d",
                    cClass.toString(), cMethod.toString(), logicDataModelTypeCount));
        } else if (gameSessionTypeCount > 1) {
            throw new GameInitializeException(String.format("逻辑函数配置错误，GameSession 参数个数大于 1. :: %s.%s %d",
                    cClass.toString(), cMethod.toString(), gameSessionTypeCount));
        } else if (sessionMessageTypeCount > 1) {
            throw new GameInitializeException(String.format("逻辑函数配置错误，SessionMessage 参数个数大于 1. :: %s.%s %d",
                    cClass.toString(), cMethod.toString(), sessionMessageTypeCount));
        } else if (unknowTypeCount > 0) {
            throw new GameInitializeException(String.format("逻辑函数配置错误，unknowTypeCount 参数个数大于 0. :: %s.%s %d",
                    cClass.toString(), cMethod.toString(), unknowTypeCount));
        } else if (logicDataModelTypeCount == 0 && gameSessionTypeCount == 0) {
            throw new GameInitializeException(String.format("逻辑函数配置错误，ILogicDataModel 参数与 GameSession 必须指定一个. :: %s.%s",
                    cClass.toString(), cMethod.toString()));
        } else if ((cMethod.getModifiers() & Modifier.PUBLIC) == 0) { throw new GameInitializeException(
                String.format("逻辑函数配置错误，修饰符必须是 PUBLIC. :: %s.%s", cClass.toString(), cMethod.toString())); }
                
        for (Class<?> pt : parametersTypes) {
            if (MessageLite.class.isAssignableFrom(pt)) acceptMessageType = pt;
            else if (ILogicDataModel.class.isAssignableFrom(pt)) acceptDataModelType = pt;
        }
    }
    
    public LogicHandleCallsite buildCallsite() {
        StringBuilder methodDefine = new StringBuilder();
        methodDefine.append("public boolean invoke(").append("org.server.backend.session.GameSession session")
                .append(",").append("org.server.core.io.SessionMessage message").append(",")
                .append("org.server.backend.io.handle.ILogicDataModel model").append(",")
                .append("com.google.protobuf.MessageLite generateMessage").append(")").append("{");
                
        methodDefine.append("if (acceptDataModelType!=null && !acceptDataModelType.equals(model.getClass())) return false;");
        methodDefine.append("if (!acceptMessageType.equals(generateMessage.getClass())) return false;");
        
        if ((cMethod.getModifiers() & Modifier.STATIC) == 0)
            methodDefine.append("this.target.").append(cMethod.getName());
        else methodDefine.append(cClass.getName()).append(cMethod.getName());
        methodDefine.append("(");
        for (Class<?> pt : parametersTypes) {
            if (GameSession.class.equals(pt)) methodDefine.append("session").append(",");
            else if (SessionMessage.class.equals(pt)) methodDefine.append("message").append(",");
            else if (ILogicDataModel.class.isAssignableFrom(pt)) methodDefine.append("(")
                    .append(acceptDataModelType.getName().replace("$", ".")).append(")").append("model").append(",");
            else if (MessageLite.class.isAssignableFrom(pt))
                methodDefine.append("(").append(acceptMessageType.getName().replace("$", ".")).append(")")
                        .append("generateMessage").append(",");
            else throw new GameInitializeException("buildCallsite error , unknow type " + pt);
        }
        methodDefine.delete(methodDefine.length() - 1, methodDefine.length());
        methodDefine.append(");");
        methodDefine.append("return true;");
        methodDefine.append("}");
        
        StringBuilder constructorDefine = new StringBuilder("public ").append(getClassSimpleName()).append("(")
                .append(cClass.getName()).append(" target").append(", ").append("Class acceptMessageType").append(", ")
                .append("Class acceptDataModelType").append(")").append("{").append("this(target);")
                .append("this.acceptMessageType=acceptMessageType;")
                .append("this.acceptDataModelType=acceptDataModelType;").append("}");
                
        return (LogicHandleCallsite) buildCallsite0((p, x) -> {
            x.addInterface(p.get(LogicHandleCallsite.class.getName()));
            x.addField(CtField.make("Class acceptMessageType;", x));
            x.addField(CtField.make("Class acceptDataModelType;", x));
            x.addMethod(CtMethod.make("public Class getAcceptMessageType() {return acceptMessageType;}", x));
            x.addMethod(CtMethod.make("public Class getAcceptDataModelType() {return acceptDataModelType;}", x));
            x.addConstructor(CtNewConstructor.make(constructorDefine.toString(), x));
            x.addMethod(CtMethod.make(methodDefine.toString(), x));
        } , context, acceptMessageType, acceptDataModelType);
    }
}
