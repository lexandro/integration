package com.lexandro.integration.service.exception;

public class EventUnmarshallingException extends ImaginariumException {
    public EventUnmarshallingException(String message) {
        super(message);
    }

    public EventUnmarshallingException(String message, Throwable e) {
        super(message, e);
    }

}
