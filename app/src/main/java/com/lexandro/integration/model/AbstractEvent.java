package com.lexandro.integration.model;

import lombok.Data;

@Data
public abstract class AbstractEvent {

    private EventType eventType;
    private Marketplace marketplace;
    private User creator;
    private String returnUrl;
    private String flag; // development or not? good to know

}
