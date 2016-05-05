package org.server.backend.io.handle.impl.callsite;

import org.server.backend.io.handle.ILogicCallsite;
import org.server.backend.io.handle.ILogicDataModel;
import org.server.backend.session.GameSession;
import org.server.core.io.SessionMessage;

import com.google.protobuf.MessageLite;

public interface LogicHandleCallsite extends ILogicCallsite {
    
    public Class<?> getAcceptMessageType();
    
    public Class<?> getAcceptDataModelType();
    
    public boolean invoke(GameSession session, SessionMessage message, ILogicDataModel model,
            MessageLite generateMessage);
}
