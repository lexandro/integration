package com.lexandro.integration.service.event.strategy;

import com.lexandro.integration.model.ApplicationUser;
import com.lexandro.integration.model.EventResponse;
import com.lexandro.integration.model.UnAssignUserEvent;
import com.lexandro.integration.service.user.UserService;
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

public class UnAssignUserStrategyTest {

    @InjectMocks
    private UnAssignUserStrategy unAssignUserStrategy;

    @Mock
    private XmlService xmlService;

    @Mock
    private UserService userService;

    private String orderEventXml;

    private UnAssignUserEvent unAssignUserEvent;
    private ApplicationUser applicationUser;

    @Before
    public void setUp() throws Exception {
        unAssignUserStrategy = new UnAssignUserStrategy();
        initMocks(this);
        //
        orderEventXml = "<blah>USER_UNASSIGNMENT</blah>";
        //
        unAssignUserEvent = new UnAssignUserEvent();
        when(xmlService.toObject(orderEventXml, UnAssignUserEvent.class)).thenReturn(unAssignUserEvent);
        //
        applicationUser = new ApplicationUser();
        applicationUser.setAccountId("testAccountId");
        when(userService.unAssign(unAssignUserEvent)).thenReturn(applicationUser);
    }

    @Test
    public void shouldReturnTrueForApplicationUserCancelContent() throws Exception {
        // when
        Boolean actualResult = unAssignUserStrategy.apply("<blah>USER_UNASSIGNMENT</blah>");
        // then
        assertTrue(actualResult);
    }

    @Test
    public void shouldReturnFalseForNonCancelMessage() throws Exception {
        // when
        Boolean actualResult = unAssignUserStrategy.apply("<blah>BLABHALBHABLA</blah>");
        // then
        assertFalse(actualResult);
    }


    @Test
    public void shouldReturnFalseForEmptyMessage() throws Exception {
        // when
        Boolean actualResult = unAssignUserStrategy.apply("");
        // then
        assertFalse(actualResult);
    }

    @Test
    public void shouldReturnFalseForNull() throws Exception {
        // when
        Boolean actualResult = unAssignUserStrategy.apply(null);
        // then
        assertFalse(actualResult);
    }

    @Test
    public void shouldProcessConvertXmlToCancelEvent() throws Exception {
        // when
        unAssignUserStrategy.process(orderEventXml);
        // then
        verify(xmlService).toObject(orderEventXml, UnAssignUserEvent.class);
    }

    @Test
    public void shouldCallCancellationWithConvertedEventObject() throws Exception {
        // when
        unAssignUserStrategy.process(orderEventXml);
        // then
        verify(userService).unAssign(unAssignUserEvent);
    }

    @Test
    public void shouldReturnCancellationSuccessResult() throws Exception {
        // given
        EventResponse expectedResult = new EventResponse();
        expectedResult.setSuccess(true);
        expectedResult.setMessage("User unassigned");
        // when
        EventResponse actualResult = unAssignUserStrategy.process(orderEventXml);
        // then
        assertEquals(expectedResult, actualResult);
    }
}