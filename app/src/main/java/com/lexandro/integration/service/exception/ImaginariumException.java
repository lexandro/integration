package com.lexandro.integration.service.exception;

// Marker exception for business cases
public class ImaginariumException extends RuntimeException {

    public ImaginariumException(String message) {
        super(message);
    }

    public ImaginariumException(String message, Throwable e) {
        super(message, e);
    }

}
