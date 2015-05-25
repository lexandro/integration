package com.lexandro.integration.service.event.strategy;

import com.lexandro.integration.model.EventResponse;
import com.lexandro.integration.model.Subscription;
import com.lexandro.integration.model.SubscriptionCreateEvent;
import com.lexandro.integration.model.SubscriptionCreateEvent;
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

public class CreateSubscriptionStrategyTest {

    @InjectMocks
    private CreateSubscriptionStrategy createSubscriptionStrategy;

    @Mock
    private XmlService xmlService;

    @Mock
    private SubscriptionService subscriptionService;

    private String orderEventXml;

    private SubscriptionCreateEvent subscriptionCreateEvent;
    private Subscription createdSubscription;

    @Before
    public void setUp() throws Exception {
        createSubscriptionStrategy = new CreateSubscriptionStrategy();
        initMocks(this);
        //
        orderEventXml = "<blah>SUBSCRIPTION_ORDER</blah>";
        //
        subscriptionCreateEvent = new SubscriptionCreateEvent();
        when(xmlService.toObject(orderEventXml, SubscriptionCreateEvent.class)).thenReturn(subscriptionCreateEvent);
        //
        createdSubscription = new Subscription();
        createdSubscription.setAccountId("testAccountId");
        when(subscriptionService.create(subscriptionCreateEvent)).thenReturn(createdSubscription);
    }

    @Test
    public void shouldReturnTrueForSubscriptionCreateContent() throws Exception {
        // when
        Boolean actualResult = createSubscriptionStrategy.apply("<blah>SUBSCRIPTION_ORDER</blah>");
        // then
        assertTrue(actualResult);
    }

    @Test
    public void shouldReturnFalseForNonCreateMessage() throws Exception {
        // when
        Boolean actualResult = createSubscriptionStrategy.apply("<blah>BLABHALBHABLA</blah>");
        // then
        assertFalse(actualResult);
    }


    @Test
    public void shouldReturnFalseForEmptyMessage() throws Exception {
        // when
        Boolean actualResult = createSubscriptionStrategy.apply("");
        // then
        assertFalse(actualResult);
    }

    @Test
    public void shouldReturnFalseForNull() throws Exception {
        // when
        Boolean actualResult = createSubscriptionStrategy.apply(null);
        // then
        assertFalse(actualResult);
    }

    @Test
    public void shouldProcessConvertXmlToCreateEvent() throws Exception {
        // when
        createSubscriptionStrategy.process(orderEventXml);
        // then
        verify(xmlService).toObject(orderEventXml, SubscriptionCreateEvent.class);
    }

    @Test
    public void shouldCallCreateWithConvertedEventObject() throws Exception {
        // when
        createSubscriptionStrategy.process(orderEventXml);
        // then
        verify(subscriptionService).create(subscriptionCreateEvent);
    }

    @Test
    public void shouldReturnCreateSuccessResult() throws Exception {
        // given
        EventResponse expectedResult = new EventResponse();
        expectedResult.setSuccess(true);
        expectedResult.setAccountIdentifier(createdSubscription.getAccountId());
        expectedResult.setMessage("Subscription created");
        // when
        EventResponse actualResult = createSubscriptionStrategy.process(orderEventXml);
        // then
        assertEquals(expectedResult, actualResult);
    }
}