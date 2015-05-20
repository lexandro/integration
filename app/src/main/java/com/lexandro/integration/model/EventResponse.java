package com.lexandro.integration.model;

import lombok.Data;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@Getter
@Data
@XmlRootElement(name = "response")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class EventResponse {
    private String accountIdentifier;
    private boolean success;
    private ErrorCode errorCode;
    private String message;


}