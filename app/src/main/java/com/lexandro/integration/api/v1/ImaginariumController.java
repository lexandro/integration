package com.lexandro.integration.api.v1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = ImaginariumController.BASE_PATH)
public class ImaginariumController {

    protected static final String BASE_PATH = "/imaginarium/";

    @RequestMapping(value = "hello", method = RequestMethod.GET)
    public String sayHello() {
        System.out.println("Hello");
        return "hello";
    }
}
