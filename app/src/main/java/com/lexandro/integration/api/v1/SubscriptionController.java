package com.lexandro.integration.api.v1;

import com.wordnik.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.lexandro.integration.api.v1.Version.VERSION;

@RestController("SubscriptionController" + VERSION)
@RequestMapping(SubscriptionController.BASE_PATH)
@Api(value = "SubscriptionController-" + VERSION, description = "API gateway for Appdirect's Subscription Management Api")
@Slf4j
public class SubscriptionController {

    protected static final String BASE_PATH = "/imaginarium/" + VERSION;

    @RequestMapping(value = "/create/{eventUrl}", method = RequestMethod.GET)
    public ResponseEntity create(@PathVariable("buildingId") String eventUrl) {

        log.info("Event URL: {}", eventUrl);
        return null;
    }

    public void change() {

    }

    public void cancel() {

    }

    public void status() {

    }


}
