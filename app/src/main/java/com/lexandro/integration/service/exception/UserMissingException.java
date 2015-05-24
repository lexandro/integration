package com.lexandro.integration.service.exception;

public class UserMissingException extends ImaginariumException {

    public UserMissingException(String id) {
        super(String.format("User missing  with Id: %s", id));
    }
}
