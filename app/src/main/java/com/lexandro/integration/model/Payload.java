package com.lexandro.integration.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "payload")
public class Payload {
    private Company company;
    // FIXME what is this configuration thingy?
    // private PayloadConfiguration configuration;
    private Order order;
}
