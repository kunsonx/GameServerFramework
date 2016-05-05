package org.server.backend.io.handle.impl.layer;

import org.server.backend.BackendServer;
import org.server.backend.io.handle.impl.TransportHandlerTask;
import org.server.backend.io.handle.impl.exception.TransportException;
import org.server.backend.session.GameSession;
import org.server.core.io.SessionMessage;

public class TransportLayer extends AbstractLayer {
    
    @Override
    public void in(GameSession session, SessionMessage message, TransportHandlerTask task) throws TransportException {
    }
    
    @Override
    public void out(GameSession session, SessionMessage message, TransportHandlerTask task) throws TransportException {
        BackendServer.getInstance().getBackendRMIServerInterface().write(session.getBackendSession(), message);
    }
}
