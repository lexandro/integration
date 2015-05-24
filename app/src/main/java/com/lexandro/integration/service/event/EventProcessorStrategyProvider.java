package com.lexandro.integration.service.event;

import com.lexandro.integration.service.event.strategy.EventProcessorStrategy;

import java.util.Optional;

public interface EventProcessorStrategyProvider {

    Optional<EventProcessorStrategy> get(String rawXml);
}
