package com.lexandro.integration.api.v1;

import com.wordnik.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.lexandro.integration.api.v1.ApiVersion.VERSION;

@RestController("ImaginariumController" + VERSION)
@RequestMapping(value = ImaginariumController.BASE_PATH)
@Api(value = "Imaginarium-" + VERSION, description = "Basic API version for Imaginarium")
public class ImaginariumController {

    protected static final String BASE_PATH = "/imaginarium/" + VERSION;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public void sayHello() {
        System.out.println("Hello");

    }
}
