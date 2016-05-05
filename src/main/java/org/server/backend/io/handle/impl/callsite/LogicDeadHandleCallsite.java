package org.server.backend.io.handle.impl.callsite;

import org.server.backend.io.handle.impl.exception.TransportException;
import org.server.backend.session.GameSession;
import org.server.core.io.SessionMessage;

import com.google.protobuf.MessageLite;

public interface LogicDeadHandleCallsite {
    
    public void invoke(GameSession session, SessionMessage message, MessageLite messageLite,
            TransportException transportException);
}
