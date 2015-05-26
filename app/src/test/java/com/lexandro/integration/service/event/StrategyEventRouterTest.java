package com.lexandro.integration.service.event;

import com.lexandro.integration.model.ErrorCode;
import com.lexandro.integration.model.EventResponse;
import com.lexandro.integration.model.logging.EventLog;
import com.lexandro.integration.repository.EventLoggingRepository;
import com.lexandro.integration.service.event.strategy.EventProcessorStrategy;
import com.lexandro.integration.service.exception.AccountMissingException;
import com.lexandro.integration.service.exception.UserExistsException;
import com.lexandro.integration.service.exception.UserMissingException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.xml.bind.JAXBException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class StrategyEventRouterTest {


    public static final String TEST_EVENT_URL = "testEventUrl";
    public static final String RAW_EVENT = "rawEvent";
    @InjectMocks
    private StrategyEventRouter strategyEventRouter;

    @Mock
    private EventLoggingRepository eventLoggingRepository;
    @Mock
    private EventService eventService;
    @Mock
    private EventProcessorStrategyProvider eventProcessorStrategyProvider;
    @Mock
    private EventProcessorStrategy mockStrategy;
    @Mock
    private EventResponse mockResponse;


    @Before
    public void setUp() throws Exception {
        strategyEventRouter = new StrategyEventRouter();
        initMocks(this);
        //
        when(eventService.get(TEST_EVENT_URL)).thenReturn(RAW_EVENT);
        //
        when(eventProcessorStrategyProvider.get(RAW_EVENT)).thenReturn(Optional.of(mockStrategy));
    }

    @Test
    public void shouldReturnResulFromGivenStrategy() throws Exception {
        // given
        when(mockStrategy.process(RAW_EVENT)).thenReturn(mockResponse);
        // when
        EventResponse actualResult = strategyEventRouter.routeEvent(TEST_EVENT_URL);
        // then
        assertEquals(mockResponse, actualResult);
    }

    @Test
    public void shouldRouteGetsEventViaEventService() throws Exception {
        // when
        strategyEventRouter.routeEvent(TEST_EVENT_URL);
        // then
        verify(eventService).get(TEST_EVENT_URL);
    }

    @Test
    public void shouldStoreEventInDB() throws Exception {
        // given
        ArgumentCaptor<EventLog> argument = ArgumentCaptor.forClass(EventLog.class);
        // when
        strategyEventRouter.routeEvent(TEST_EVENT_URL);
        // then
        verify(eventLoggingRepository).save(argument.capture());
        assertEquals(TEST_EVENT_URL, argument.getValue().getUrl());
        assertEquals(RAW_EVENT, argument.getValue().getXml());
    }

    @Test
    public void shouldReturnFalseEventResultWhenRawEventIsEmpty() throws Exception {
        // given
        when(eventService.get(TEST_EVENT_URL)).thenReturn("");
        // when
        EventResponse actualResult = strategyEventRouter.routeEvent(TEST_EVENT_URL);
        // then
        assertFalse(actualResult.isSuccess());
        assertEquals("Error reading event data", actualResult.getMessage());
        assertEquals(ErrorCode.UNKNOWN_ERROR, actualResult.getErrorCode());
    }

    @Test
    public void shouldReturnFalseEventResultWhenRawEventIsNull() throws Exception {
        // given
        when(eventService.get(TEST_EVENT_URL)).thenReturn(null);
        // when
        EventResponse actualResult = strategyEventRouter.routeEvent(TEST_EVENT_URL);
        // then
        assertFalse(actualResult.isSuccess());
        assertEquals("Error reading event data", actualResult.getMessage());
        assertEquals(ErrorCode.UNKNOWN_ERROR, actualResult.getErrorCode());
    }

    @Test
    public void shouldAskForStrategyToProcessEvent() throws Exception {
        // when
        strategyEventRouter.routeEvent(TEST_EVENT_URL);
        // then
        verify(eventProcessorStrategyProvider).get(RAW_EVENT);
    }

    @Test
    public void shouldProcessRawEventWithReceivedStrategy() throws Exception {
        // when
        strategyEventRouter.routeEvent(TEST_EVENT_URL);
        // then
        verify(mockStrategy).process(RAW_EVENT);
    }

    @Test
    public void shouldReturnUnknownEventMessageIfNoStrategyFound() throws Exception {
        // given
        when(eventProcessorStrategyProvider.get(RAW_EVENT)).thenReturn(Optional.empty());
        // when
        EventResponse actualResult = strategyEventRouter.routeEvent(TEST_EVENT_URL);
        // then
        assertFalse(actualResult.isSuccess());
        assertEquals("Received unknown event", actualResult.getMessage());
        assertEquals(ErrorCode.INVALID_RESPONSE, actualResult.getErrorCode());
    }

    @Test
    public void shouldTranslateJaxbExceptionToUnknownError() throws Exception {
        //given
        when(mockStrategy.process(RAW_EVENT)).thenThrow(new JAXBException("error"));
        // when
        EventResponse actualResult = strategyEventRouter.routeEvent(TEST_EVENT_URL);
        // then
        assertFalse(actualResult.isSuccess());
        assertEquals("Error reading event data", actualResult.getMessage());
        assertEquals(ErrorCode.UNKNOWN_ERROR, actualResult.getErrorCode());
    }


    @Test
    public void shouldTranslateIllegalArgumentExceptionToUnknownError() throws Exception {
        //given
        when(mockStrategy.process(RAW_EVENT)).thenThrow(new IllegalArgumentException("error"));
        // when
        EventResponse actualResult = strategyEventRouter.routeEvent(TEST_EVENT_URL);
        // then
        assertFalse(actualResult.isSuccess());
        assertEquals("Error reading event data", actualResult.getMessage());
        assertEquals(ErrorCode.UNKNOWN_ERROR, actualResult.getErrorCode());
    }

    @Test
    public void shouldTranslateUserExistsExceptionUserAlreadyExistsError() throws Exception {
        //given
        when(mockStrategy.process(RAW_EVENT)).thenThrow(new UserExistsException("error"));
        // when
        EventResponse actualResult = strategyEventRouter.routeEvent(TEST_EVENT_URL);
        // then
        assertFalse(actualResult.isSuccess());
        assertEquals("User already exists", actualResult.getMessage());
        assertEquals(ErrorCode.USER_ALREADY_EXISTS, actualResult.getErrorCode());
    }

    @Test
    public void shouldTranslateUserMissingExceptionUserMissingError() throws Exception {
        //given
        when(mockStrategy.process(RAW_EVENT)).thenThrow(new UserMissingException("error"));
        // when
        EventResponse actualResult = strategyEventRouter.routeEvent(TEST_EVENT_URL);
        // then
        assertFalse(actualResult.isSuccess());
        assertEquals("User missing", actualResult.getMessage());
        assertEquals(ErrorCode.USER_NOT_FOUND, actualResult.getErrorCode());
    }

    @Test
    public void shouldTranslateAccountMissingExceptionAccountNotFoundError() throws Exception {
        //given
        when(mockStrategy.process(RAW_EVENT)).thenThrow(new AccountMissingException("error"));
        // when
        EventResponse actualResult = strategyEventRouter.routeEvent(TEST_EVENT_URL);
        // then
        assertFalse(actualResult.isSuccess());
        assertEquals("Account missing", actualResult.getMessage());
        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, actualResult.getErrorCode());
    }
}
