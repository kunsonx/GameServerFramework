package org.server.backend.io.handle.impl.callsite;

import org.server.backend.io.handle.ILogicDataModel;
import org.server.backend.session.GameSession;
import org.server.core.io.SessionMessage;

import com.google.protobuf.MessageLite;

public interface LogicAuthorizeCallsite {
    
    public Class<?> returnDataModel();
    
    public ILogicDataModel invoke(GameSession session, SessionMessage message, MessageLite messageLite);
}
