package com.lexandro.integration.service;

import com.lexandro.integration.model.EventResponse;
import com.lexandro.integration.model.SubscriptionCreateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class AppDirectSubscriptionService implements SubscriptionService {

    @Resource
    private EventService eventService;

    @Override
    public EventResponse create(String eventUrl) {
        log.debug("Subscriptionservice create with {}", eventUrl);
        SubscriptionCreateEvent subscriptionCreateEvent = eventService.get(eventUrl, SubscriptionCreateEvent.class);
        return null;
    }

    @Override
    public EventResponse change(String eventUrl) {
        return null;
    }

    @Override
    public EventResponse cancel(String eventUrl) {
        return null;
    }

    @Override
    public EventResponse status(String eventUrl) {
        return null;
    }
}
