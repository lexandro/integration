package com.lexandro.integration.service;

import com.lexandro.integration.model.*;
import com.lexandro.integration.service.exception.UserExistsException;
import com.lexandro.integration.service.exception.UserMissingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

@Service
@Slf4j
public class XmlEventRouter implements EventRouter {

    @Resource
    private EventService eventService;

    @Resource
    private XmlService xmlService;

    @Resource
    private SubscriptionService subscriptionService;


    @Override
    public EventResponse routeEvent(String eventUrl) {
        String rawEvent = eventService.get(eventUrl);
        EventResponse result = new EventResponse();
        result.setSuccess(true);
        // consider strategy pattern for routing
        try {
            // speed optimized solution, to avoid double xml parsing
            if (rawEvent.contains("SUBSCRIPTION_ORDER")) {
                createSubscription(rawEvent, result);
            } else if (rawEvent.contains("SUBSCRIPTION_CHANGE")) {
                changeSubscription(rawEvent, result);
            }
        } catch (JAXBException e) {
            log.error("Error unmarshalling XML event from eventUrl {} error: {}", eventUrl, e);
            result.setSuccess(false);
            result.setMessage("Error reading event data");
            result.setErrorCode(ErrorCode.UNKNOWN_ERROR);
        } catch (UserExistsException uee) {
            result.setSuccess(false);
            result.setMessage("User already exists");
            result.setErrorCode(ErrorCode.USER_ALREADY_EXISTS);
        } catch (UserMissingException uee) {
            result.setSuccess(false);
            result.setMessage("User missing");
            result.setErrorCode(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        return result;
    }

    private void createSubscription(String rawEvent, EventResponse result) throws JAXBException {
        SubscriptionCreateEvent subscriptionEvent = xmlService.toObject(rawEvent, SubscriptionCreateEvent.class);
        Subscription createdSubcription = subscriptionService.create(subscriptionEvent);
        //
        result.setAccountIdentifier(createdSubcription.getId());
        result.setMessage("Subscription created");

        log.info("Subscription created: {}", result);
    }

    private void changeSubscription(String rawEvent, EventResponse result) throws JAXBException {
        SubscriptionChangeEvent subscriptionEvent = xmlService.toObject(rawEvent, SubscriptionChangeEvent.class);
        Subscription changedSubscription = subscriptionService.change(subscriptionEvent);
        //
        result.setAccountIdentifier(changedSubscription.getId());
        result.setMessage("Subscription changed");

        log.info("Subscription changed: {}", result);
    }
}
