package com.lexandro.integration.model;

import lombok.Data;

@Data
public abstract class AbstractEvent {

    protected EventType eventType;
    protected Marketplace marketplace;
    protected User creator;
    protected String returnUrl;
    protected String flag; // development or not? good to know

}
