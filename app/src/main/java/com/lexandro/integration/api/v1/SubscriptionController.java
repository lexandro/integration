package com.lexandro.integration.api.v1;

import com.lexandro.integration.model.EventResponse;
import com.lexandro.integration.service.router.EventRouter;
import com.wordnik.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth.provider.ConsumerAuthentication;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.lexandro.integration.api.v1.ApiVersion.VERSION;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController("SubscriptionController" + VERSION)
@RequestMapping(value = SubscriptionController.BASE_PATH, produces = APPLICATION_XML_VALUE)
@Api(value = "SubscriptionController-" + VERSION, description = "API gateway for AppDirect's Subscription Management Api")
@Slf4j
public class SubscriptionController {

    protected static final String BASE_PATH = "/subscription/" + VERSION;
    protected static final String EVENT_URL_PARAM_VALUE = "url";
    protected static final String TOKEN_PARAM_VALUE = "token";

    @Resource
    private EventRouter eventRouter;


    @RequestMapping(value = "/create", method = GET)
    public ResponseEntity<EventResponse> create(@RequestParam(value = EVENT_URL_PARAM_VALUE) String eventUrl, @RequestParam(value = TOKEN_PARAM_VALUE) String token, @AuthenticationPrincipal ConsumerAuthentication authentication) {
        log.info("Called create event URL: {}, auth: {}, token: {}", eventUrl, authentication, token);
        //
        EventResponse createResponse = eventRouter.routeEvent(eventUrl);
        //
        return new ResponseEntity<>(createResponse, OK);
    }

    @RequestMapping(value = "/change", method = GET)
    public EventResponse change(@RequestParam(value = EVENT_URL_PARAM_VALUE) String eventUrl, @RequestParam(value = TOKEN_PARAM_VALUE) String token, @AuthenticationPrincipal ConsumerAuthentication authentication) {
        log.info("Called change event URL: {}, auth: {}, token: {}", eventUrl, authentication, token);
        //
        EventResponse changeResponse = eventRouter.routeEvent(eventUrl);
        //
        return changeResponse;
    }

    @RequestMapping(value = "/cancel", method = GET)
    public ResponseEntity cancel(@RequestParam(value = EVENT_URL_PARAM_VALUE) String eventUrl, @RequestParam(value = TOKEN_PARAM_VALUE) String token, @AuthenticationPrincipal ConsumerAuthentication authentication) {
        log.info("Called cancel event URL: {}, auth: {}, token: {}", eventUrl, authentication, token);
        //
        EventResponse cancelResponse = eventRouter.routeEvent(eventUrl);
        //
        return new ResponseEntity<>(cancelResponse, OK);
    }

    @RequestMapping(value = "/status", method = GET)
    public ResponseEntity status(@RequestParam(value = EVENT_URL_PARAM_VALUE) String eventUrl, @RequestParam(value = TOKEN_PARAM_VALUE) String token, @AuthenticationPrincipal ConsumerAuthentication authentication) {
        log.info("Called status event URL: {}, auth: {}, token: {}", eventUrl, authentication, token);
        //
        EventResponse statusResponse = eventRouter.routeEvent(eventUrl);
        //
        return new ResponseEntity<>(statusResponse, OK);
    }

}
