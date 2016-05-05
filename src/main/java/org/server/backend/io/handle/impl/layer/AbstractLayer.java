package org.server.backend.io.handle.impl.layer;

import org.server.backend.io.handle.ITransportHandlerLayer;

public abstract class AbstractLayer implements ITransportHandlerLayer {
    
    ITransportHandlerLayer previous;
    ITransportHandlerLayer next;
                           
    @Override
    public ITransportHandlerLayer previous() {
        return previous;
    }
    
    @Override
    public void previous(ITransportHandlerLayer previous) {
        this.previous = previous;
    }
    
    @Override
    public ITransportHandlerLayer next() {
        return next;
    }
    
    @Override
    public void next(ITransportHandlerLayer next) {
        this.next = next;
    }
    
}
