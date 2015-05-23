package com.lexandro.integration.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class Account {
    private String accountIdentifier;
    private AccountStatus status;
}
