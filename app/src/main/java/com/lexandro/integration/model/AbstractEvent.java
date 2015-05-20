package com.lexandro.integration.model;

import lombok.Data;

@Data
public abstract class AbstractEvent {

    private EventType eventType;
    private Marketplace marketplace;
    private String returnUrl;

}
