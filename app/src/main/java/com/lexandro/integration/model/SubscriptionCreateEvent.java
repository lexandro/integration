package com.lexandro.integration.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "event")
public class SubscriptionCreateEvent extends AbstractEvent {

    private final EventType eventType = EventType.SUBSCRIPTION_ORDER;
    private User creator;
    private Payload payload;


}
