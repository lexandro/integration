package com.lexandro.integration.service;

import org.springframework.security.oauth.common.OAuthException;
import org.springframework.security.oauth.provider.BaseConsumerDetails;
import org.springframework.security.oauth.provider.ConsumerDetails;
import org.springframework.security.oauth.provider.ConsumerDetailsService;

public class AppDirectConsumerDetailsService implements ConsumerDetailsService {


    @Override
    public ConsumerDetails loadConsumerByConsumerKey(String consumerKey) throws OAuthException {
        BaseConsumerDetails result = new BaseConsumerDetails();
        result.setRequiredToObtainAuthenticatedToken(false);
        return result;
    }
}
