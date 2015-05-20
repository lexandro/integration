package com.lexandro.integration.model;


import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class Marketplace {

    private String partner;
    private String baseUrl;
}
