package com.lexandro.integration.service.router;

import com.lexandro.integration.model.EventResponse;

public interface EventRouter {

    EventResponse routeEvent(String eventUrl);

}
