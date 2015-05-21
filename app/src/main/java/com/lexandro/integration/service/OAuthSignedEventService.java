package com.lexandro.integration.service;

import com.lexandro.integration.model.AbstractEvent;
import lombok.extern.slf4j.Slf4j;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

@Service
@Slf4j
public class OAuthSignedEventService implements EventService {

    @Resource
    private OAuthConsumer oAuthConsumer;

    @Resource
    private HttpService httpService;

    @Override
    public <T extends AbstractEvent> T get(String url, Class<T> resultType) {
        log.debug("OAuthSignedEventService get with {}", url);
        try {
            String signedUrl = oAuthConsumer.sign(url);
            log.debug("Signed URL {}", signedUrl);
            String result = httpService.get(signedUrl);
            log.error("get result {}", result);
        } catch (OAuthMessageSignerException | OAuthExpectationFailedException | OAuthCommunicationException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
