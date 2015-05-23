package com.lexandro.integration.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class Company {
    private String uuid;
    private String country;
    private String name;
    private String phoneNumber;
    private String email;
    private String website;

}
