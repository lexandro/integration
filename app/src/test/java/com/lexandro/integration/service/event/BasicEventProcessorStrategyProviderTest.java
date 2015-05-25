package com.lexandro.integration.service.event;

import com.lexandro.integration.service.event.strategy.EventProcessorStrategy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by lexandro on 5/25/15.
 */
public class BasicEventProcessorStrategyProviderTest {

    public static final String TEST_XML = "test";
    private BasicEventProcessorStrategyProvider basicEventProcessorStrategyProvider;

    private List<EventProcessorStrategy> eventProcessorStrategyList;

    @Mock
    private EventProcessorStrategy alwaysFalseStrategy;

    @Mock
    private EventProcessorStrategy alwaysTrueStrategy;


    @Before
    public void setUp() throws Exception {
        basicEventProcessorStrategyProvider = new BasicEventProcessorStrategyProvider();
        initMocks(this);
        //
        eventProcessorStrategyList = new ArrayList<>();
        //
        when(alwaysFalseStrategy.apply(TEST_XML)).thenReturn(false);
        when(alwaysTrueStrategy.apply(TEST_XML)).thenReturn(true);
    }

    @Test
    public void shouldReturnEmptyWHenNoStrategyProvided() throws Exception {
        // given
        // when
        Optional<EventProcessorStrategy> actualResult = basicEventProcessorStrategyProvider.get(TEST_XML);
        // then
        assertFalse(actualResult.isPresent());
    }

    @Test
    public void shouldReturnEmptyWhenStrategyListIsEmpty() throws Exception {
        // given
        ReflectionTestUtils.setField(basicEventProcessorStrategyProvider, "eventProcessorStrategyList", eventProcessorStrategyList);
        // when
        Optional<EventProcessorStrategy> actualResult = basicEventProcessorStrategyProvider.get(TEST_XML);
        // then
        assertFalse(actualResult.isPresent());
    }

    @Test
    public void shouldReturnEmptyWhenStrategyListHasNoApplicableStrategyElement() throws Exception {
        // given

        eventProcessorStrategyList.add(alwaysFalseStrategy);
        ReflectionTestUtils.setField(basicEventProcessorStrategyProvider, "eventProcessorStrategyList", eventProcessorStrategyList);
        // when
        Optional<EventProcessorStrategy> actualResult = basicEventProcessorStrategyProvider.get(TEST_XML);
        // then
        assertFalse(actualResult.isPresent());
    }

    @Test
    public void shouldReturnAnItemWhenStrategyListHaspplicableStrategyElement() throws Exception {
        // given
        eventProcessorStrategyList.add(alwaysFalseStrategy);
        eventProcessorStrategyList.add(alwaysTrueStrategy);
        ReflectionTestUtils.setField(basicEventProcessorStrategyProvider, "eventProcessorStrategyList", eventProcessorStrategyList);
        // when
        Optional<EventProcessorStrategy> actualResult = basicEventProcessorStrategyProvider.get(TEST_XML);
        // then
        assertEquals(alwaysTrueStrategy, actualResult.get());
    }
}