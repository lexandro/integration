package com.lexandro.integration.service.event;

import com.lexandro.integration.model.AbstractEvent;
import com.lexandro.integration.service.HttpService;
import com.lexandro.integration.service.exception.EventReadingException;
import com.lexandro.integration.service.exception.EventUnmarshallingException;
import com.lexandro.integration.service.xml.XmlService;
import lombok.extern.slf4j.Slf4j;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;
import java.io.IOException;

@Service
@Slf4j
// FIXME catch blocks
public class OAuthSignedEventService implements EventService {

    @Resource
    private OAuthConsumer oAuthConsumer;

    @Resource
    private HttpService httpService;

    @Resource
    private XmlService xmlService;

    @Override
    public <T extends AbstractEvent> T get(String url, Class<T> expectedEventType) {
        log.debug("OAuthSignedEventService get with {}", url);
        String result = "";
        try {
            result = get(url);
            log.debug("Got raw result: {}", result);
            //
            T eventObject = xmlService.toObject(result, expectedEventType);
            log.debug("Unmarshalled result {}", eventObject);
            //
            return eventObject;
        } catch (JAXBException e) {
            throw new EventUnmarshallingException("Error unmarshalling eventObject from: " + result, e);
        }
    }

    @Override
    public String get(String url) {
        log.debug("OAuthSignedEventService get raw data with {}", url);
        try {
            Assert.hasText(url);
            String signedUrl = oAuthConsumer.sign(url);
            log.debug("Signed URL {}", signedUrl);
            return httpService.get(signedUrl);
        } catch (OAuthExpectationFailedException | OAuthCommunicationException | OAuthMessageSignerException | IOException e) {
            throw new EventReadingException("Error reading event from: " + url, e);
        } catch (IllegalArgumentException e) {
            throw new EventReadingException("Url is null or empty", e);
        }
    }
}
