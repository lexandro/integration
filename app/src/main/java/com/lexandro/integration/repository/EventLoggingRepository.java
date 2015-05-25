package com.lexandro.integration.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventLoggingRepository extends MongoRepository<String, String> {

}
