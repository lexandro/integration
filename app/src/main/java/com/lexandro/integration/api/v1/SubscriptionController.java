package com.lexandro.integration.api.v1;

import com.lexandro.integration.model.EventResponse;
import com.wordnik.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.lexandro.integration.api.v1.Version.VERSION;

@RestController("SubscriptionController" + VERSION)
@RequestMapping(value = SubscriptionController.BASE_PATH)
@Api(value = "SubscriptionController-" + VERSION, description = "API gateway for AppDirect's Subscription Management Api")
@Slf4j
public class SubscriptionController {

    protected static final String BASE_PATH = "/subscription/" + VERSION;


    @RequestMapping(value = "/create", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<EventResponse> create(@RequestParam(value = "eventUrl", required = true) String eventUrl) {
        log.info("Called create event URL: {}", eventUrl);
        //
        EventResponse response = new EventResponse();
        response.setSuccess(true);
        response.setMessage("The application is alive!");
        //
        return new ResponseEntity<EventResponse>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/change", method = RequestMethod.GET)
    public ResponseEntity change(@RequestParam(value = "eventUrl", required = true) String eventUrl) {
        log.info("Change event URL: {}", eventUrl);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.GET)
    public ResponseEntity cancel(@RequestParam(value = "eventUrl", required = true) String eventUrl) {
        log.info("Cancel event URL: {}", eventUrl);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public ResponseEntity status(@RequestParam(value = "eventUrl", required = true) String eventUrl) {
        log.info("Status event URL: {}", eventUrl);
        return new ResponseEntity(HttpStatus.OK);
    }

}
