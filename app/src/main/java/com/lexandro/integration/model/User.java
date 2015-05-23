package com.lexandro.integration.model;


import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlRootElement
public class User {

    private String email;
    private String firstName;
    private String lastName;
    private String language;


    private String openId;
    private String uuid;

    private List<AttributeEntry> attributes;
}
