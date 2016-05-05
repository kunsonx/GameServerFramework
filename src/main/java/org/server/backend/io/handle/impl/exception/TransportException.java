package org.server.backend.io.handle.impl.exception;

public class TransportException extends RuntimeException {
    
    private static final long serialVersionUID = -7355700498791707343L;
    
    public TransportException(String message) {
        super(message);
    }
    
    public TransportException(Throwable cause) {
        super(cause);
    }
}
