package com.lexandro.integration.service.event;


import com.lexandro.integration.service.event.strategy.EventProcessorStrategy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class BasicEventProcessorStrategyProvider implements EventProcessorStrategyProvider {


    @Resource
    private List<EventProcessorStrategy> eventProcessorStrategyList;

    @Override
    public Optional<EventProcessorStrategy> get(String rawXml) {
        if (eventProcessorStrategyList != null) {
            return eventProcessorStrategyList
                    .stream()
                    .filter(strategy -> strategy.apply(rawXml))
                    .findFirst();
        } else {
            return Optional.empty();
        }
    }
}
