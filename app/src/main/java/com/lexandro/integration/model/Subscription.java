package com.lexandro.integration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Subscription {

    @Id
    private String id;

    @Indexed(unique = true)
    private String accountId;
    //
    private Marketplace marketplace;
    private User creator;
    //
    private Account account;
    private Company company;
    private Order order;
    private Notice notice;

}
