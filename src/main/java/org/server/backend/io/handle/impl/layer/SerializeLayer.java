package org.server.backend.io.handle.impl.layer;

import static org.server.backend.io.handle.impl.TransportMeta.*;

import org.server.backend.io.handle.annotation.LogicModelMeta;
import org.server.backend.io.handle.impl.TransportHandlerTask;
import org.server.backend.io.handle.impl.exception.TransportException;
import org.server.backend.session.GameSession;
import org.server.core.io.SessionMessage;

import com.google.protobuf.MessageLite;

public class SerializeLayer extends AbstractLayer {
    
    @Override
    public void in(GameSession session, SessionMessage message, TransportHandlerTask task) throws TransportException {
        LogicModelMeta meta = MODEL_IN_META_TABLE.get(Integer.valueOf(message.getCommand()));
        if (meta != null) {
            try {
                Object model = meta.serialize().deserialization(meta.owner(), message.getData());
                task.setGeneratedMessage((MessageLite) model);
            } catch (Throwable e) {
                throw new TransportException(e);
            }
        } else {
            throw new TransportException("can't find from MODEL_IN_META_TABLE.. " + message.getCommand());
        }
    }
    
    @Override
    public void out(GameSession session, SessionMessage message, TransportHandlerTask task) throws TransportException {
        LogicModelMeta meta = MODEL_OUT_META_TABLE.get(task.getGeneratedMessage().getClass());
        if (meta != null) {
            try {
                byte[] data = meta.serialize().serialize(task.getGeneratedMessage());
                SessionMessage msg = new SessionMessage((short) meta.command(), data);
                task.setMessage(msg);
            } catch (Throwable e) {
                throw new TransportException(e);
            }
        } else {
            throw new TransportException("can't find from MODEL_OUT_META_TABLE.. " + message.getCommand());
        }
    }
}
