package com.lexandro.integration.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum Notice {

    DEACTIVATED, REACTIVATED, CLOSED, UPCOMING_INVOICE
}
