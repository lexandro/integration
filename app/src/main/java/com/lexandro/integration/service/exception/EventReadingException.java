package com.lexandro.integration.service.exception;

public class EventReadingException extends ImaginariumException {
    public EventReadingException(String message) {
        super(message);
    }

    public EventReadingException(String message, Throwable e) {
        super(message, e);
    }

}
