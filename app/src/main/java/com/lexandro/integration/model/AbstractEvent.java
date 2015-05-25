package com.lexandro.integration.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "event")
public abstract class AbstractEvent {

    protected EventType eventType;
    protected Marketplace marketplace;
    protected User creator;
    protected String returnUrl;
    protected String flag; // development or not? good to know

}
