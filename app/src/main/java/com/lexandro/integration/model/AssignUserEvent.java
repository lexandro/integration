package com.lexandro.integration.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "event")
@EqualsAndHashCode(callSuper = false)
public class AssignUserEvent extends AbstractEvent {

    private final EventType eventType = EventType.USER_ASSIGNMENT;
    private Payload payload;
}
