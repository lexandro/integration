package com.lexandro.integration.service.event.strategy;

import com.lexandro.integration.model.ApplicationUser;
import com.lexandro.integration.model.AssignUserEvent;
import com.lexandro.integration.model.EventResponse;
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

public class AssignUserStrategyTest {

    @InjectMocks
    private AssignUserStrategy assignUserStrategy;

    @Mock
    private XmlService xmlService;

    @Mock
    private UserService userService;

    private String orderEventXml;

    private AssignUserEvent assignUserEvent;
    private ApplicationUser applicationUser;

    @Before
    public void setUp() throws Exception {
        assignUserStrategy = new AssignUserStrategy();
        initMocks(this);
        //
        orderEventXml = "<blah>USER_ASSIGNMENT</blah>";
        //
        assignUserEvent = new AssignUserEvent();
        when(xmlService.toObject(orderEventXml, AssignUserEvent.class)).thenReturn(assignUserEvent);
        //
        applicationUser = new ApplicationUser();
        applicationUser.setAccountId("testAccountId");
        when(userService.assign(assignUserEvent)).thenReturn(applicationUser);
    }

    @Test
    public void shouldReturnTrueForApplicationUserCancelContent() throws Exception {
        // when
        Boolean actualResult = assignUserStrategy.apply("<blah>USER_ASSIGNMENT</blah>");
        // then
        assertTrue(actualResult);
    }

    @Test
    public void shouldReturnFalseForNonCancelMessage() throws Exception {
        // when
        Boolean actualResult = assignUserStrategy.apply("<blah>BLABHALBHABLA</blah>");
        // then
        assertFalse(actualResult);
    }


    @Test
    public void shouldReturnFalseForEmptyMessage() throws Exception {
        // when
        Boolean actualResult = assignUserStrategy.apply("");
        // then
        assertFalse(actualResult);
    }

    @Test
    public void shouldReturnFalseForNull() throws Exception {
        // when
        Boolean actualResult = assignUserStrategy.apply(null);
        // then
        assertFalse(actualResult);
    }

    @Test
    public void shouldProcessConvertXmlToCancelEvent() throws Exception {
        // when
        assignUserStrategy.process(orderEventXml);
        // then
        verify(xmlService).toObject(orderEventXml, AssignUserEvent.class);
    }

    @Test
    public void shouldCallCancellationWithConvertedEventObject() throws Exception {
        // when
        assignUserStrategy.process(orderEventXml);
        // then
        verify(userService).assign(assignUserEvent);
    }

    @Test
    public void shouldReturnCancellationSuccessResult() throws Exception {
        // given
        EventResponse expectedResult = new EventResponse();
        expectedResult.setSuccess(true);
        expectedResult.setMessage("User assigned");
        // when
        EventResponse actualResult = assignUserStrategy.process(orderEventXml);
        // then
        assertEquals(expectedResult, actualResult);
    }
}