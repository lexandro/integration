package com.lexandro.integration.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum EventType {
    SUBSCRIPTION_ORDER,
    SUBSCRIPTION_CHANGE,
    SUBSCRIPTION_CANCEL,
    SUBSCRIPTION_NOTICE
}
