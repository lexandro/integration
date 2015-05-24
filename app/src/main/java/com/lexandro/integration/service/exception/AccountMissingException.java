package com.lexandro.integration.service.exception;

public class AccountMissingException extends ImaginariumException {

    public AccountMissingException(String id) {
        super(String.format("Account missing with Id: %s", id));
    }
}
