package com.lexandro.integration.service;

import com.lexandro.integration.model.EventResponse;

public interface SubscriptionService {

    EventResponse create(String eventUrl);

    EventResponse change(String eventUrl);

    EventResponse cancel(String eventUrl);

    EventResponse status(String eventUrl);
}
