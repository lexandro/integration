package com.lexandro.integration.service.event;

import com.lexandro.integration.model.AbstractEvent;

public interface EventService {

    <T extends AbstractEvent> T get(String url, Class<T> resultType);

    String get(String url);
}
