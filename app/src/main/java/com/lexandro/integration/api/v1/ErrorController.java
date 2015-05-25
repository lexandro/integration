package com.lexandro.integration.api.v1;

import lombok.extern.slf4j.Slf4j;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ErrorController {

    public static final String DEFAULT_ERROR_VIEW = "error";

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(OAuthMessageSignerException.class)
    public String handleConflict(Model model) {
        model.addAttribute("message", "Unauthorized access");
        return DEFAULT_ERROR_VIEW;
    }
}
