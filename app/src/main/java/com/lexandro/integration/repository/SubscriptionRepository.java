package com.lexandro.integration.repository;

import com.lexandro.integration.model.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubscriptionRepository extends MongoRepository<Subscription, String> {

    Subscription findByAccountId(String accountId);

}
