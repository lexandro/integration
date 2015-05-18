package com.lexandro.integration.api;

import com.wordnik.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("OrderOptimizerController" + ImaginariumController.VERSION)
@RequestMapping(ImaginariumController.BASE_PATH)
@Api(value = "Imaginarium-" + ImaginariumController.VERSION, description = "Basic API version for Imaginarium")
public class ImaginariumController {

    protected static final String VERSION = "v1";
    protected static final String BASE_PATH = "/imaginarium/" + VERSION;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public void sayHello() {
        System.out.println("Hello");

    }
}
