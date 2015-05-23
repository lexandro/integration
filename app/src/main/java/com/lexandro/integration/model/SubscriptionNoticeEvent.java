package com.lexandro.integration.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "event")
@EqualsAndHashCode(callSuper = false)
public class SubscriptionNoticeEvent extends AbstractEvent {

    private final EventType eventType = EventType.SUBSCRIPTION_NOTICE;
    private Payload payload;

}