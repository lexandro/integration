package com.lexandro.integration.api.v1;

import com.wordnik.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static com.lexandro.integration.api.v1.ApiVersion.VERSION;

@Controller
@RequestMapping(value = LoginController.BASE_PATH)
@Api(value = "Login-" + VERSION, description = "Basic API version for Logging in into Imaginarium")
public class LoginController {

    protected static final String BASE_PATH = "/";

    @RequestMapping(value = "/loginpage", method = RequestMethod.GET)
    public String doLogin() {
        System.out.println("Hello");
        return "login";

    }

    @RequestMapping(value = "/logoutpage", method = RequestMethod.GET)
    public String doLogout() {
        System.out.println("Bye");
        return "logout";

    }
}
