package com.lexandro.integration.api.v1;

import com.wordnik.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.lexandro.integration.api.v1.Version.VERSION;

@RestController("SubscriptionController" + VERSION)
@RequestMapping(value = SubscriptionController.BASE_PATH)
@Api(value = "SubscriptionController-" + VERSION, description = "API gateway for AppDirect's Subscription Management Api")
@Slf4j
public class SubscriptionController {

    protected static final String BASE_PATH = "/subscription/" + VERSION;


    @RequestMapping("/create")
    public ResponseEntity create(@RequestParam String eventUrl) {
        log.info("Create event URL: {}", eventUrl);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/change/{eventUrl}", method = RequestMethod.GET)
    public ResponseEntity change(@PathVariable("eventUrl") String eventUrl) {
        log.info("Change event URL: {}", eventUrl);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/cancel/{eventUrl}", method = RequestMethod.GET)
    public ResponseEntity cancel(@PathVariable("eventUrl") String eventUrl) {
        log.info("Cancel event URL: {}", eventUrl);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/status/{eventUrl}", method = RequestMethod.GET)
    public ResponseEntity status(@PathVariable("eventUrl") String eventUrl) {
        log.info("Status event URL: {}", eventUrl);
        return new ResponseEntity(HttpStatus.OK);
    }

}
