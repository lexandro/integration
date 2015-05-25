package com.lexandro.integration.service.event.strategy;

import com.lexandro.integration.model.EventResponse;
import com.lexandro.integration.model.EventType;
import com.lexandro.integration.model.Subscription;
import com.lexandro.integration.model.SubscriptionCreateEvent;
import com.lexandro.integration.service.subscription.SubscriptionService;
import com.lexandro.integration.service.xml.XmlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

@Service
@Slf4j
public class CreateSubscriptionStrategy implements EventProcessorStrategy {

    @Resource
    private XmlService xmlService;

    @Resource
    private SubscriptionService subscriptionService;

    @Override
    public Boolean apply(String xmlString) {
        return xmlString != null && xmlString.contains(EventType.SUBSCRIPTION_ORDER.toString());
    }

    @Override
    public EventResponse process(String rawXml) throws JAXBException {
        SubscriptionCreateEvent subscriptionEvent = xmlService.toObject(rawXml, SubscriptionCreateEvent.class);
        //
        Subscription createdSubscription = subscriptionService.create(subscriptionEvent);
        //
        EventResponse result = new EventResponse();
        result.setSuccess(true);
        result.setAccountIdentifier(createdSubscription.getAccountId());
        result.setMessage("Subscription created");
        //
        log.info("Subscription created: {}", result);
        return result;
    }
}
