package com.lexandro.integration.service.event;

import java.util.Optional;

public interface EventProcessorStrategyProvider {

    Optional<EventProcessorStrategy> get(String rawXml);
}
