package com.lexandro.integration.service;

import com.lexandro.integration.model.AbstractEvent;
import lombok.extern.slf4j.Slf4j;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.springframework.stereotype.Service;

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
        try {
            String result = get(url);
            log.debug("Got raw result: {}", result);
            //
            T eventObject = xmlService.toObject(result, expectedEventType);
            log.debug("Unmarshalled result {}", eventObject);
            //
            return eventObject;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String get(String url) {
        log.debug("OAuthSignedEventService get raw data with {}", url);
        try {
            String signedUrl = oAuthConsumer.sign(url);
            log.debug("Signed URL {}", signedUrl);
            return httpService.get(signedUrl);
        } catch (OAuthExpectationFailedException | OAuthCommunicationException | OAuthMessageSignerException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
