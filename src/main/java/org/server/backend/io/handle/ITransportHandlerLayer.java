package org.server.backend.io.handle;

import org.server.backend.io.handle.impl.TransportHandlerTask;
import org.server.backend.io.handle.impl.exception.TransportException;
import org.server.backend.session.GameSession;
import org.server.core.io.SessionMessage;

public interface ITransportHandlerLayer {
    
    public void previous(ITransportHandlerLayer previous);
    
    public ITransportHandlerLayer previous();
    
    public void next(ITransportHandlerLayer next);
    
    public ITransportHandlerLayer next();
    
    public void in(GameSession session, SessionMessage message, TransportHandlerTask task) throws TransportException;
    
    public void out(GameSession session, SessionMessage message, TransportHandlerTask task) throws TransportException;
}
