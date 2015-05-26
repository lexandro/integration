package com.lexandro.integration.service.event;

import com.lexandro.integration.model.SubscriptionCreateEvent;
import com.lexandro.integration.service.HttpService;
import com.lexandro.integration.service.exception.EventReadingException;
import com.lexandro.integration.service.exception.EventUnmarshallingException;
import com.lexandro.integration.service.xml.XmlService;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class OAuthSignedEventServiceTest {

    public static final String TEST_URL = "testUrl";
    public static final String SIGNED_TEST_URL = "signedUrl";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @InjectMocks
    private OAuthSignedEventService oAuthSignedEventService;


    @Mock
    private OAuthConsumer oAuthConsumer;

    @Mock
    private HttpService httpService;

    @Mock
    private XmlService xmlService;

    @Mock
    private SubscriptionCreateEvent subscriptionCreateEvent;
    private String testXml;

    @Before
    public void setUp() throws Exception {
        oAuthSignedEventService = new OAuthSignedEventService();
        initMocks(this);
        //
        when(oAuthConsumer.sign(TEST_URL)).thenReturn(SIGNED_TEST_URL);
        //
        testXml = "testXml";
        when(httpService.get(SIGNED_TEST_URL)).thenReturn(testXml);
        //
        when(xmlService.toObject(testXml, SubscriptionCreateEvent.class)).thenReturn(subscriptionCreateEvent);
    }

    @Test
    public void shouldGetSignsTheUrl() throws Exception {
        // when
        oAuthSignedEventService.get(TEST_URL);
        // then
        verify(oAuthConsumer).sign(TEST_URL);
    }

    @Test
    public void shouldCallHttpServiceWithSignedUrl() throws Exception {
        // given
        // when
        oAuthSignedEventService.get(TEST_URL);
        // then
        verify(httpService).get(SIGNED_TEST_URL);
    }

    @Test
    public void shouldNotAcceptEmptyUrl() throws Exception {
        // given
        expectedException.expect(EventReadingException.class);
        expectedException.expectMessage("Url is null or empty");
        // when
        oAuthSignedEventService.get("");
    }

    @Test
    public void shouldNotAcceptNullUrl() throws Exception {
        // given
        expectedException.expect(EventReadingException.class);
        expectedException.expectMessage("Url is null or empty");
        // when
        oAuthSignedEventService.get(null);
    }

    @Test
    public void shouldWrapOAuthExpectationFailedException() throws Exception {
        // given
        expectedException.expect(EventReadingException.class);
        expectedException.expectMessage("Error reading event from:");
        when(oAuthConsumer.sign("faultyUrl")).thenThrow(new OAuthExpectationFailedException("oops"));
        // when
        oAuthSignedEventService.get("faultyUrl");
    }

    @Test
    public void shouldWrapOAuthCommunicationException() throws Exception {
        // given
        expectedException.expect(EventReadingException.class);
        expectedException.expectMessage("Error reading event from:");
        when(oAuthConsumer.sign("faultyUrl")).thenThrow(new OAuthCommunicationException("msg", "body"));
        // when
        oAuthSignedEventService.get("faultyUrl");
    }

    @Test
    public void shouldWrapOAuthMessageSignerException() throws Exception {
        // given
        expectedException.expect(EventReadingException.class);
        expectedException.expectMessage("Error reading event from:");
        when(oAuthConsumer.sign("faultyUrl")).thenThrow(new OAuthMessageSignerException("oops"));
        // when
        oAuthSignedEventService.get("faultyUrl");
    }

    @Test
    public void shouldWrapIOException() throws Exception {
        // given
        expectedException.expect(EventReadingException.class);
        expectedException.expectMessage("Error reading event from:");
        when(httpService.get(SIGNED_TEST_URL)).thenThrow(new IOException());
        // when
        oAuthSignedEventService.get(TEST_URL);
    }

    @Test
    public void shouldGetCallXMLServiceToUnmarshall() throws Exception {
        // when
        SubscriptionCreateEvent actualResult = oAuthSignedEventService.get(TEST_URL, SubscriptionCreateEvent.class);
        // then
        verify(xmlService).toObject(testXml, SubscriptionCreateEvent.class);
        assertEquals(subscriptionCreateEvent, actualResult);
    }

    @Test
    public void shouldWrapJaxbException() throws Exception {
        // given
        expectedException.expect(EventUnmarshallingException.class);
        expectedException.expectMessage("Error unmarshalling eventObject from: " + testXml);
        when(xmlService.toObject(testXml, SubscriptionCreateEvent.class)).thenThrow(new JAXBException("error"));
        // when
        oAuthSignedEventService.get(TEST_URL, SubscriptionCreateEvent.class);
    }

    @Test
    public void shouldGetWithClassNotAcceptNullUrl() throws Exception {
        // given
        expectedException.expect(EventReadingException.class);
        expectedException.expectMessage("Url is null or empty");
        // when
        oAuthSignedEventService.get(null, SubscriptionCreateEvent.class);
        // then     
    }

    @Test
    public void shouldGetWithClassNotAcceptEmptyUrl() throws Exception {
        // given
        expectedException.expect(EventReadingException.class);
        expectedException.expectMessage("Url is null or empty");
        // when
        oAuthSignedEventService.get("", SubscriptionCreateEvent.class);
        // then
    }
}
