package com.lexandro.integration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Subscription {

    @Id
    private String id;
    //
    private Marketplace marketplace;
    // FIXME do we need to store creator to a subscription data?
    private User creator;
    //
    private Account account;
    private Company company;
    private Order order;

}
