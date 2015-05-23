package com.lexandro.integration.service.event;

import com.lexandro.integration.model.EventResponse;

public interface EventRouter {

    EventResponse routeEvent(String eventUrl);

}
