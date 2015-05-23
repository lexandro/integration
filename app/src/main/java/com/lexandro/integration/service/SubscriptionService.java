package com.lexandro.integration.service;

import com.lexandro.integration.model.EventResponse;
import com.lexandro.integration.model.SubscriptionCreateEvent;

public interface SubscriptionService {

    EventResponse create(SubscriptionCreateEvent subscriptionCreateEvent);

    EventResponse change(String eventUrl);

    EventResponse cancel(String eventUrl);

    EventResponse status(String eventUrl);
}
