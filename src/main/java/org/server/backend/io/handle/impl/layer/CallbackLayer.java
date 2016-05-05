package org.server.backend.io.handle.impl.layer;

import org.server.backend.session.GameSession;
import org.server.core.io.SessionMessage;
import org.server.backend.io.handle.impl.TransportHandlerTask;
import org.server.backend.io.handle.impl.TransportMeta;
import org.server.backend.io.handle.impl.callsite.LogicAuthorizeCallsite;
import org.server.backend.io.handle.impl.callsite.LogicHandleCallsite;
import org.server.backend.io.handle.impl.exception.AuthorizeException;
import org.server.backend.io.handle.impl.exception.TransportException;
import org.server.backend.io.handle.ILogicDataModel;

public class CallbackLayer extends AbstractLayer {
    
    static ILogicDataModel EMPTY = new ILogicDataModel() {
    };
    
    @Override
    public void in(GameSession session, SessionMessage message, TransportHandlerTask task) throws TransportException {
        LogicHandleCallsite handleCallsite = TransportMeta
                .findHandleCallsiteWithAcceptMessageType(task.getGeneratedMessage().getClass());
        if (handleCallsite != null) {
            LogicAuthorizeCallsite authorizeCallsite = TransportMeta
                    .findAuthorizeWithReturnType(handleCallsite.getAcceptDataModelType());
            ILogicDataModel dataModel = EMPTY;
            if (authorizeCallsite != null) {
                dataModel = authorizeCallsite.invoke(session, message, task.getGeneratedMessage());
                if (dataModel == null) throw new AuthorizeException("authorize fail , drop message ...");
            }
            boolean success = handleCallsite.invoke(session, message, dataModel, task.getGeneratedMessage());
            if (!success) throw new TransportException(
                    "invoke handlecallsite fail:: " + task.getGeneratedMessage().getClass().getName());
        } else {
            throw new TransportException(
                    "can't find message type handlecallsite :: " + task.getGeneratedMessage().getClass().getName());
        }
    }
    
    @Override
    public void out(GameSession session, SessionMessage message, TransportHandlerTask task) throws TransportException {
    }
}
