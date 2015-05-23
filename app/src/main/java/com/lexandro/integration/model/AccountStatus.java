package com.lexandro.integration.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum AccountStatus {
    FREE_TRIAL, FREE_TRIAL_EXPIRED, ACTIVE, CANCELLED, SUSPENDED
}
