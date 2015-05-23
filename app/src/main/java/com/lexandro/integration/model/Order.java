package com.lexandro.integration.model;


import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Order {

    private String editionCode;
    private String pricingDuration;
    @XmlElement(name = "item")
    private List<OrderItem> items;
}
