package com.lexandro.integration.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "payload")
public class Payload {
    private Company company;
    // FIXME what is this configuration thingy? It was always empty. Could be extended later if necessary
    // private PayloadConfiguration configuration;
    private Order order;
    private Account account;
    private Notice notice;
    private User user;
}
