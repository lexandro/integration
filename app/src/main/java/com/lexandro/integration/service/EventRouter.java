package com.lexandro.integration.service;

import com.lexandro.integration.model.EventResponse;

public interface EventRouter {

    EventResponse routeEvent(String eventUrl);

}
