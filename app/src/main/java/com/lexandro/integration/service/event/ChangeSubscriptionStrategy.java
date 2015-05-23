package com.lexandro.integration.service.event;

import com.lexandro.integration.model.EventResponse;
import com.lexandro.integration.model.EventType;
import com.lexandro.integration.model.Subscription;
import com.lexandro.integration.model.SubscriptionChangeEvent;
import com.lexandro.integration.service.subscription.SubscriptionService;
import com.lexandro.integration.service.xml.XmlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

@Service
@Slf4j
public class ChangeSubscriptionStrategy implements EventProcessorStrategy {

    @Resource
    private XmlService xmlService;

    @Resource
    private SubscriptionService subscriptionService;

    @Override
    public Boolean apply(String xmlString) {
        return xmlString.contains(EventType.SUBSCRIPTION_CHANGE.toString());
    }

    @Override
    public EventResponse process(String rawXml) throws JAXBException {
        SubscriptionChangeEvent subscriptionEvent = xmlService.toObject(rawXml, SubscriptionChangeEvent.class);
        Subscription changedSubscription = subscriptionService.change(subscriptionEvent);
        //
        EventResponse result = new EventResponse();
        result.setSuccess(true);
        result.setAccountIdentifier(changedSubscription.getId());
        result.setMessage("Subscription changed");

        log.info("Subscription changed: {}", result);
        return result;
    }
}
