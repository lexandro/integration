package com.lexandro.integration.model;

import lombok.Data;

@Data

public class OrderItem {

    private String unit;
    // FIXME double? Bigdecimal?
    private long quantity;
}
