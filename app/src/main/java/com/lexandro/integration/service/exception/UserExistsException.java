package com.lexandro.integration.service.exception;

import com.lexandro.integration.model.Subscription;


public class UserExistsException extends RuntimeException {

    public UserExistsException(Subscription subscription) {
        super(String.format("Subscription can't be saved with Id: %s", subscription.getCreator().getOpenId()));
    }
}
