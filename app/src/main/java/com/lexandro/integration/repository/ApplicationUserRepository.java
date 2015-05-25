package com.lexandro.integration.repository;

import com.lexandro.integration.model.ApplicationUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ApplicationUserRepository extends MongoRepository<ApplicationUser, String> {

    ApplicationUser findByAccountIdAndOpenId(String accountId, String openId);

    List<ApplicationUser> findByAccountId(String findByAccountId);

    ApplicationUser findByOpenId(String openId);

    String deleteByAccountId(String accountId);
}
