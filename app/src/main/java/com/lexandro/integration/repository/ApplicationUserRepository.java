package com.lexandro.integration.repository;

import com.lexandro.integration.model.ApplicationUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApplicationUserRepository extends MongoRepository<ApplicationUser, String> {

    ApplicationUser findByAccountIdAndOpenId(String accountId, String openId);

    void deleteByAccountId(String accountIdentifier);
}
