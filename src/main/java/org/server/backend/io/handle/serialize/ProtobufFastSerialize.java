package org.server.backend.io.handle.serialize;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.server.backend.io.handle.ILogicModelSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.MessageLite;

public class ProtobufFastSerialize implements ILogicModelSerialize {
    
    interface ParseFromBytes {
        Object parseFormData(byte[] data);
    }
    
    static final Logger                 log = LoggerFactory.getLogger(ProtobufFastSerialize.class);
                                            
    final Class<MessageLite>            messageBaseClass;
    final Map<Class<?>, ParseFromBytes> parseFromBytesFunc;
    final MethodHandles.Lookup          caller;
                                        
    public ProtobufFastSerialize() {
        messageBaseClass = MessageLite.class;
        parseFromBytesFunc = new HashMap<>();
        caller = MethodHandles.lookup();
    }
    
    @Override
    public boolean isSupport(Class<?> type) {
        return messageBaseClass.isAssignableFrom(type);
    }
    
    @Override
    public byte[] serialize(Object obj) {
        Objects.requireNonNull(obj, "param obj can't be null.");
        if (isSupport(obj.getClass())) return ((MessageLite) obj).toByteArray();
        else return null;
    }
    
    @Override
    public Object deserialization(Class<?> type, byte[] data) {
        Objects.requireNonNull(type, "param type can't be null.");
        Objects.requireNonNull(data, "param data can't be null.");
        if (!isSupport(type)) return null;
        ParseFromBytes func = parseFromBytesFunc.get(type);
        if (func == null) {
            synchronized (parseFromBytesFunc) {
                func = makeDeserializationFunc(type, func);
                // save func to global table
                parseFromBytesFunc.put(type, func);
            }
        }
        return func.parseFormData(data);
    }
    
    private ParseFromBytes makeDeserializationFunc(Class<?> type, ParseFromBytes func) {
        try {
            MethodType originFuncArgs = MethodType.methodType(type, byte[].class);
            MethodType targetFuncArgs = MethodType.methodType(Object.class, byte[].class);
            MethodType targetClass = MethodType.methodType(ParseFromBytes.class);
            MethodHandle originFuncHandle = caller.findStatic(type, "parseFrom", originFuncArgs);
            CallSite callSite = LambdaMetafactory.metafactory(caller, "parseFormData", targetClass, targetFuncArgs,
                    originFuncHandle, originFuncArgs);
            MethodHandle factory = callSite.getTarget();
            func = (ParseFromBytes) factory.invoke();
        } catch (Throwable e) {
            log.error("makeDeserializationFunc error ..", e);
        }
        return func;
    }
    
}
