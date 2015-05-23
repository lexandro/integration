package com.lexandro.integration.api.v1;

import com.lexandro.integration.model.EventResponse;
import com.lexandro.integration.service.event.EventRouter;
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
import static com.lexandro.integration.api.v1.AppDirectConstants.EVENT_URL_PARAM_VALUE;
import static com.lexandro.integration.api.v1.AppDirectConstants.TOKEN_PARAM_VALUE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController("UserController" + VERSION)
@RequestMapping(value = UserController.BASE_PATH, produces = APPLICATION_XML_VALUE)
@Api(value = "UserController-" + VERSION, description = "API gateway for AppDirect's User Management Api")
@Slf4j
public class UserController {

    protected static final String BASE_PATH = "/users/" + VERSION;

    @Resource
    private EventRouter eventRouter;

    @RequestMapping(value = "/assign", method = GET)
    public ResponseEntity<EventResponse> create(@RequestParam(value = EVENT_URL_PARAM_VALUE) String eventUrl, @RequestParam(value = TOKEN_PARAM_VALUE) String token, @AuthenticationPrincipal ConsumerAuthentication authentication) {
        log.info("Called user assign event URL: {}, auth: {}, token: {}", eventUrl, authentication, token);
        //
        EventResponse assignResponse = eventRouter.routeEvent(eventUrl);
        //
        return new ResponseEntity<>(assignResponse, OK);
    }

    @RequestMapping(value = "/unassign", method = GET)
    public ResponseEntity<EventResponse> unassign(@RequestParam(value = EVENT_URL_PARAM_VALUE) String eventUrl, @RequestParam(value = TOKEN_PARAM_VALUE) String token, @AuthenticationPrincipal ConsumerAuthentication authentication) {
        log.info("Called user unassign event URL: {}, auth: {}, token: {}", eventUrl, authentication, token);
        //
        EventResponse unassignResponse = eventRouter.routeEvent(eventUrl);
        //
        return new ResponseEntity<>(unassignResponse, OK);
    }
}
