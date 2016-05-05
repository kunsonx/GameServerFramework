package org.server.backend.io.handle.impl.callsite;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.server.backend.component.GameInitializeException;
import org.server.backend.io.handle.ILogicDataModel;
import org.server.backend.session.GameSession;
import org.server.core.io.SessionMessage;

import com.google.protobuf.MessageLite;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewConstructor;

public abstract class AbstractLogicCallsiteFactory {
    
    @FunctionalInterface
    public interface ConfigNewClass {
        public void config(ClassPool p, CtClass newClass) throws Throwable;
    }
    
    @FunctionalInterface
    public interface ChooseConstructor {
        public Object choose(Constructor<?>[] constructors) throws Throwable;
    }
    
    Object     context;
    Class<?>   cClass;
    Method     cMethod;
    Class<?>[] parametersTypes;
               
    Class<?>   implType;
               
    protected AbstractLogicCallsiteFactory() {
    }
    
    public AbstractLogicCallsiteFactory(Object target, Class<?> componentClass, Method method,
            Class<?>[] parametersTypes) {
        this.context = target;
        this.cClass = componentClass;
        this.cMethod = method;
        this.parametersTypes = parametersTypes;
    }
    
    public void validateCallbackArgs() {
        long messageTypeCount = 0;
        long logicDataModelTypeCount = 0;
        long gameSessionTypeCount = 0;
        long sessionMessageTypeCount = 0;
        long unknowTypeCount = 0;
        Class<?> returnType = null;
        
        if ((cMethod.getModifiers() & Modifier.PUBLIC) == 0) throw new GameInitializeException(String.format(
                "callsite verify error，method must be public access :: %s.%s", cClass.toString(), cMethod.toString()));
                
        for (Class<?> x : parametersTypes) {
            if (MessageLite.class.isAssignableFrom(x)) messageTypeCount++;
            else if (ILogicDataModel.class.isAssignableFrom(x)) logicDataModelTypeCount++;
            else if (GameSession.class.equals(x)) gameSessionTypeCount++;
            else if (SessionMessage.class.equals(x)) sessionMessageTypeCount++;
            else unknowTypeCount++;
        }
        
        returnType = cMethod.getReturnType();
        
        validateCallbackArgs0(messageTypeCount, logicDataModelTypeCount, gameSessionTypeCount, sessionMessageTypeCount,
                unknowTypeCount, returnType);
    }
    
    protected String getClassSimpleName() {
        String className = cMethod.getName().substring(0, 1).toUpperCase() + cMethod.getName().substring(1) + "Impl";
        return className;
    }
    
    protected String getClassfullName() {
        StringBuilder classFullName = new StringBuilder();
        classFullName.append(cClass.getName().toLowerCase());
        classFullName.append(".logichandlercallback.");
        classFullName.append(getClassSimpleName());
        return classFullName.toString();
    }
    
    protected String getIncludeComponentFiledConstructor() {
        StringBuilder constructor = new StringBuilder();
        constructor.append("public ").append(getClassSimpleName()).append("(").append(cClass.getName()).append(" ")
                .append("target").append(")").append("{").append("super();").append("this.target = target;")
                .append("}");
        return constructor.toString();
    }
    
    protected String getIncludeComponentFiledDefine() {
        StringBuilder filedDefine = new StringBuilder();
        filedDefine.append("private ").append(cClass.getName()).append(" ").append("target;");
        return filedDefine.toString();
    }
    
    protected Object buildCallsite0(ConfigNewClass configNewClass, Object... ctorArgs) {
        CtClass newClass = null;
        try {
            ClassPool pool = ClassPool.getDefault();
            pool.appendSystemPath();
            newClass = pool.makeClass(getClassfullName());
            newClass.addField(CtField.make(getIncludeComponentFiledDefine(), newClass));
            newClass.addConstructor(CtNewConstructor.make(getIncludeComponentFiledConstructor(), newClass));
            
            configNewClass.config(pool, newClass);
            
            this.implType = newClass.toClass();
            Constructor<?>[] constructors = this.implType.getConstructors();
            for (Constructor<?> constructor : constructors) {
                boolean match = true;
                Class<?>[] ctorParams = constructor.getParameterTypes();
                if (ctorParams.length != ctorArgs.length) continue;
                for (int i = 0; i < ctorParams.length; i++) {
                    // 空参数不做检查
                    if (ctorArgs[i] != null && !ctorParams[i].equals(ctorArgs[i].getClass())) {
                        match = false;
                        break;
                    }
                }
                if (match) return constructor.newInstance(ctorArgs);
            }
            throw new GameInitializeException("can't find match args, newInstance impl class error . ");
        } catch (Throwable e) {
            throw new GameInitializeException("generate impl class error . ", e);
        }
    }
    
    protected abstract void validateCallbackArgs0(long messageTypeCount, long logicDataModelTypeCount,
            long gameSessionTypeCount, long sessionMessageTypeCount, long unknowTypeCount, Class<?> returnType);
}
