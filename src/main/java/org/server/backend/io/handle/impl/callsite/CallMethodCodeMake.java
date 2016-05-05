package org.server.backend.io.handle.impl.callsite;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Map.Entry;

import static org.server.tools.SystemUtil.LINE_SEPARATOR;;

public class CallMethodCodeMake {
    
    int           indent = 0;
    StringBuilder code   = new StringBuilder();
                         
    public String getIndentSpace() {
        StringBuilder space = new StringBuilder();
        for (int i = 0; i < indent; i++)
            space.append("    ");
        return space.toString();
    }
    
    public void makePublic() {
        code.append("public ");
    }
    
    public void makeReturnType(Class<?> returnType) {
        if (returnType != null) code.append(returnType.getName()).append(" ");
        else code.append("void ");
    }
    
    public void makeMethodSign(String name, Class<?> returnType, Map<Class<?>, String> params) {
        code.append(returnType.equals(Void.class) ? "void" : returnType.getName()).append(" ");
        code.append(name).append("(");
        for (Entry<Class<?>, String> item : params.entrySet())
            code.append(item.getKey().getName()).append(" ").append(item.getValue()).append(", ");
        if (!params.isEmpty()) code.delete(code.length() - 2, code.length());
        code.append(") ");
    }
    
    public void makeCodeBlockBegin() {
        code.append(getIndentSpace()).append("{").append(LINE_SEPARATOR);
        indent++;
    }
    
    public void makeCodeBlockEnd() {
        indent--;
        code.append(getIndentSpace()).append("}").append(LINE_SEPARATOR);
    }
    
    public void makeCodeLine(String codeLine) {
        code.append(getIndentSpace()).append(codeLine).append(LINE_SEPARATOR);
    }
    
    public void makeDirectInvoke(Class<?> type, Method method, Map<Class<?>, String> params) {
        StringBuilder methodDefine = new StringBuilder();
        if (!method.getReturnType().equals(Void.class)) methodDefine.append("return ");
        if ((method.getModifiers() & Modifier.STATIC) == 0)
            methodDefine.append("this.target.").append(method.getName());
        else methodDefine.append(type.getName()).append(method.getName());
        methodDefine.append("(");
        for (Class<?> pt : method.getParameterTypes()) {
            String paramName = params.get(pt);
            if (paramName != null) methodDefine.append(paramName).append(", ");
            else throw new RuntimeException("can't find match params name ! ");
        }
        if (method.getParameterTypes().length > 0)
            methodDefine.delete(methodDefine.length() - 2, methodDefine.length());
        methodDefine.append(");");
        makeCodeLine(methodDefine.toString());
    }
    
    @Override
    public String toString() {
        return code.toString();
    }
}
