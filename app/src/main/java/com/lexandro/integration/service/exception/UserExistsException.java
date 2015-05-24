package com.lexandro.integration.service.exception;

public class UserExistsException extends ImaginariumException {

    public UserExistsException(String id) {
        super(String.format("User exists with Id: %s", id));
    }
}
