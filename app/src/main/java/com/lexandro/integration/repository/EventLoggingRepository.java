package com.lexandro.integration.repository;

import com.lexandro.integration.model.logging.EventLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventLoggingRepository extends MongoRepository<EventLog, String> {

}
