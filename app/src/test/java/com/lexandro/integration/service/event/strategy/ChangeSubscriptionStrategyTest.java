package com.lexandro.integration.service.event.strategy;

import com.lexandro.integration.model.EventResponse;
import com.lexandro.integration.model.Subscription;
import com.lexandro.integration.model.SubscriptionChangeEvent;
import com.lexandro.integration.service.subscription.SubscriptionService;
import com.lexandro.integration.service.xml.XmlService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ChangeSubscriptionStrategyTest {

    @InjectMocks
    private ChangeSubscriptionStrategy changeSubscriptionStrategy;

    @Mock
    private XmlService xmlService;

    @Mock
    private SubscriptionService subscriptionService;

    private String orderEventXml;

    private SubscriptionChangeEvent subscriptionChangeEvent;
    private Subscription changedSubscription;

    @Before
    public void setUp() throws Exception {
        changeSubscriptionStrategy = new ChangeSubscriptionStrategy();
        initMocks(this);
        //
        orderEventXml = "<blah>SUBSCRIPTION_CHANGE</blah>";
        //
        subscriptionChangeEvent = new SubscriptionChangeEvent();
        when(xmlService.toObject(orderEventXml, SubscriptionChangeEvent.class)).thenReturn(subscriptionChangeEvent);
        //
        changedSubscription = new Subscription();
        changedSubscription.setAccountId("testAccountId");
        when(subscriptionService.change(subscriptionChangeEvent)).thenReturn(changedSubscription);
    }

    @Test
    public void shouldReturnTrueForSubscriptionChangeContent() throws Exception {
        // when
        Boolean actualResult = changeSubscriptionStrategy.apply("<blah>SUBSCRIPTION_CHANGE</blah>");
        // then
        assertTrue(actualResult);
    }

    @Test
    public void shouldReturnFalseForNonChangeMessage() throws Exception {
        // when
        Boolean actualResult = changeSubscriptionStrategy.apply("<blah>BLABHALBHABLA</blah>");
        // then
        assertFalse(actualResult);
    }


    @Test
    public void shouldReturnFalseForEmptyMessage() throws Exception {
        // when
        Boolean actualResult = changeSubscriptionStrategy.apply("");
        // then
        assertFalse(actualResult);
    }

    @Test
    public void shouldReturnFalseForNull() throws Exception {
        // when
        Boolean actualResult = changeSubscriptionStrategy.apply(null);
        // then
        assertFalse(actualResult);
    }

    @Test
    public void shouldProcessConvertXmlToChangeEvent() throws Exception {
        // when
        changeSubscriptionStrategy.process(orderEventXml);
        // then
        verify(xmlService).toObject(orderEventXml, SubscriptionChangeEvent.class);
    }

    @Test
    public void shouldCallChangeWithConvertedEventObject() throws Exception {
        // when
        changeSubscriptionStrategy.process(orderEventXml);
        // then
        verify(subscriptionService).change(subscriptionChangeEvent);
    }

    @Test
    public void shouldReturnChangeSuccessResult() throws Exception {
        // given
        EventResponse expectedResult = new EventResponse();
        expectedResult.setSuccess(true);
        expectedResult.setAccountIdentifier(changedSubscription.getAccountId());
        expectedResult.setMessage("Subscription changed");
        // when
        EventResponse actualResult = changeSubscriptionStrategy.process(orderEventXml);
        // then
        assertEquals(expectedResult, actualResult);
    }
}