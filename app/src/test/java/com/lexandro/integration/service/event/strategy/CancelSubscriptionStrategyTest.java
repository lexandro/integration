package com.lexandro.integration.service.event.strategy;

import com.lexandro.integration.model.EventResponse;
import com.lexandro.integration.model.Subscription;
import com.lexandro.integration.model.SubscriptionCancelEvent;
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

public class CancelSubscriptionStrategyTest {

    @InjectMocks
    private CancelSubscriptionStrategy cancelSubscriptionStrategy;

    @Mock
    private XmlService xmlService;

    @Mock
    private SubscriptionService subscriptionService;

    private String cancelEventXml;

    private SubscriptionCancelEvent subscriptionCancelEvent;
    private Subscription cancelledSubscription;

    @Before
    public void setUp() throws Exception {
        cancelSubscriptionStrategy = new CancelSubscriptionStrategy();
        initMocks(this);
        //
        cancelEventXml = "<blah>SUBSCRIPTION_CANCEL</blah>";
        //
        subscriptionCancelEvent = new SubscriptionCancelEvent();
        when(xmlService.toObject(cancelEventXml, SubscriptionCancelEvent.class)).thenReturn(subscriptionCancelEvent);
        //
        cancelledSubscription = new Subscription();
        cancelledSubscription.setAccountId("testAccountId");
        when(subscriptionService.cancel(subscriptionCancelEvent)).thenReturn(cancelledSubscription);
    }

    @Test
    public void shouldReturnTrueForSubscriptionCancelContent() throws Exception {
        // when
        Boolean actualResult = cancelSubscriptionStrategy.apply("<blah>SUBSCRIPTION_CANCEL</blah>");
        // then
        assertTrue(actualResult);
    }

    @Test
    public void shouldReturnFalseForNonCancelMessage() throws Exception {
        // when
        Boolean actualResult = cancelSubscriptionStrategy.apply("<blah>SUBSIPTION_CANCEL</blah>");
        // then
        assertFalse(actualResult);
    }


    @Test
    public void shouldReturnFalseForEmptyMessage() throws Exception {
        // when
        Boolean actualResult = cancelSubscriptionStrategy.apply("");
        // then
        assertFalse(actualResult);
    }

    @Test
    public void shouldReturnFalseForNull() throws Exception {
        // when
        Boolean actualResult = cancelSubscriptionStrategy.apply(null);
        // then
        assertFalse(actualResult);
    }

    @Test
    public void shouldProcessConvertXmlToCancelEvent() throws Exception {
        // when
        cancelSubscriptionStrategy.process(cancelEventXml);
        // then
        verify(xmlService).toObject(cancelEventXml, SubscriptionCancelEvent.class);
    }

    @Test
    public void shouldCallCancellationWithConvertedEventObject() throws Exception {
        // when
        cancelSubscriptionStrategy.process(cancelEventXml);
        // then
        verify(subscriptionService).cancel(subscriptionCancelEvent);
    }

    @Test
    public void shouldReturnCancellationSuccessResult() throws Exception {
        // given
        EventResponse expectedResult = new EventResponse();
        expectedResult.setSuccess(true);
        expectedResult.setAccountIdentifier(cancelledSubscription.getAccountId());
        expectedResult.setMessage("Subscription cancelled");
        // when
        EventResponse actualResult = cancelSubscriptionStrategy.process(cancelEventXml);
        // then
        assertEquals(expectedResult, actualResult);
    }
}