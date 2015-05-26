package com.lexandro.integration.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.oauth.provider.BaseConsumerDetails;
import org.springframework.security.oauth.provider.ConsumerDetails;

import static org.junit.Assert.*;

public class AppDirectConsumerDetailsServiceTest {

    private AppDirectConsumerDetailsService appDirectConsumerDetailsService;

    @Before
    public void setUp() throws Exception {
        appDirectConsumerDetailsService = new AppDirectConsumerDetailsService();
    }

    @Test
    public void shouldCreateBasicResult() throws Exception {
        // when
        BaseConsumerDetails actualResult = (BaseConsumerDetails) appDirectConsumerDetailsService.loadConsumerByConsumerKey("fakeData");
        // then
        assertNotNull(actualResult);
        assertFalse(actualResult.isRequiredToObtainAuthenticatedToken());
    }
}