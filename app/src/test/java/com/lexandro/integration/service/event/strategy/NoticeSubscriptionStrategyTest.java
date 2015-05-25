package com.lexandro.integration.service.event.strategy;

import com.lexandro.integration.model.EventResponse;
import com.lexandro.integration.model.Subscription;
import com.lexandro.integration.model.SubscriptionNoticeEvent;
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

public class NoticeSubscriptionStrategyTest {

    @InjectMocks
    private NoticeSubscriptionStrategy noticeSubscriptionStrategy;

    @Mock
    private XmlService xmlService;

    @Mock
    private SubscriptionService subscriptionService;

    private String orderEventXml;

    private SubscriptionNoticeEvent subscriptionNoticeEvent;
    private Subscription noticedSubscription;

    @Before
    public void setUp() throws Exception {
        noticeSubscriptionStrategy = new NoticeSubscriptionStrategy();
        initMocks(this);
        //
        orderEventXml = "<blah>SUBSCRIPTION_NOTICE</blah>";
        //
        subscriptionNoticeEvent = new SubscriptionNoticeEvent();
        when(xmlService.toObject(orderEventXml, SubscriptionNoticeEvent.class)).thenReturn(subscriptionNoticeEvent);
        //
        noticedSubscription = new Subscription();
        noticedSubscription.setAccountId("testAccountId");
        when(subscriptionService.notice(subscriptionNoticeEvent)).thenReturn(noticedSubscription);
    }

    @Test
    public void shouldReturnTrueForSubscriptionAssignContent() throws Exception {
        // when
        Boolean actualResult = noticeSubscriptionStrategy.apply("<blah>SUBSCRIPTION_NOTICE</blah>");
        // then
        assertTrue(actualResult);
    }

    @Test
    public void shouldReturnFalseForNonAssignMessage() throws Exception {
        // when
        Boolean actualResult = noticeSubscriptionStrategy.apply("<blah>BLABHALBHABLA</blah>");
        // then
        assertFalse(actualResult);
    }


    @Test
    public void shouldReturnFalseForEmptyMessage() throws Exception {
        // when
        Boolean actualResult = noticeSubscriptionStrategy.apply("");
        // then
        assertFalse(actualResult);
    }

    @Test
    public void shouldReturnFalseForNull() throws Exception {
        // when
        Boolean actualResult = noticeSubscriptionStrategy.apply(null);
        // then
        assertFalse(actualResult);
    }

    @Test
    public void shouldProcessConvertXmlToAssignEvent() throws Exception {
        // when
        noticeSubscriptionStrategy.process(orderEventXml);
        // then
        verify(xmlService).toObject(orderEventXml, SubscriptionNoticeEvent.class);
    }

    @Test
    public void shouldCallAssignWithConvertedEventObject() throws Exception {
        // when
        noticeSubscriptionStrategy.process(orderEventXml);
        // then
        verify(subscriptionService).notice(subscriptionNoticeEvent);
    }

    @Test
    public void shouldReturnAssignSuccessResult() throws Exception {
        // given
        EventResponse expectedResult = new EventResponse();
        expectedResult.setSuccess(true);
        expectedResult.setAccountIdentifier(noticedSubscription.getAccountId());
        expectedResult.setMessage("Subscription status changed");
        // when
        EventResponse actualResult = noticeSubscriptionStrategy.process(orderEventXml);
        // then
        assertEquals(expectedResult, actualResult);
    }
}