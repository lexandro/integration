package com.lexandro.integration.service.event;


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
        return eventProcessorStrategyList
                .stream()
                .filter(strategy -> strategy.apply(rawXml))
                .findFirst();
    }
}
