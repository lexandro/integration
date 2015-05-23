package com.lexandro.integration.service;

import com.lexandro.integration.model.EventResponse;
import com.lexandro.integration.model.SubscriptionCreateEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

@Service
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
        //
        try {
            // speed optimized solution, to avoid double xml parsing
            if (rawEvent.contains("SUBSCRIPTION_ORDER")) {
                SubscriptionCreateEvent subscriptionEvent = xmlService.toObject(rawEvent, SubscriptionCreateEvent.class);
                subscriptionService.create(subscriptionEvent);
            }
        } catch (JAXBException e) {
            // FIXME
        }
        return null;
    }
}
