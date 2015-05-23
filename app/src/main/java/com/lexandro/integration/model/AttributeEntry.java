package com.lexandro.integration.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class AttributeEntry {

    private String key;
    private String value;

}
