package com.lexandro.integration.service.exception;

public class UserMissingException extends RuntimeException {

    public UserMissingException(String id) {
        super(String.format("Subscription can't be loaded with Id: %s", id));
    }
}
