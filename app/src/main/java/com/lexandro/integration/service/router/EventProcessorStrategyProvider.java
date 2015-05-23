package com.lexandro.integration.service.router;

import java.util.Optional;

public interface EventProcessorStrategyProvider {

    Optional<EventProcessorStrategy> get(String rawXml);
}
