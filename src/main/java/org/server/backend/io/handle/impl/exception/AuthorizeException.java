package org.server.backend.io.handle.impl.exception;

public class AuthorizeException extends TransportException {
    
    private static final long serialVersionUID = -1623313037450886060L;
    
    public AuthorizeException(String message) {
        super(message);
    }
}
