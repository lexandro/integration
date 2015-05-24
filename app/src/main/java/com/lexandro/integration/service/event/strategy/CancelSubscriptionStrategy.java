package com.lexandro.integration.service.event.strategy;

import com.lexandro.integration.model.EventResponse;
import com.lexandro.integration.model.EventType;
import com.lexandro.integration.model.Subscription;
import com.lexandro.integration.model.SubscriptionCancelEvent;
import com.lexandro.integration.service.subscription.SubscriptionService;
import com.lexandro.integration.service.xml.XmlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

@Service
@Slf4j
public class CancelSubscriptionStrategy implements EventProcessorStrategy {

    @Resource
    private XmlService xmlService;

    @Resource
    private SubscriptionService subscriptionService;

    @Override
    public Boolean apply(String xmlString) {
        return xmlString.contains(EventType.SUBSCRIPTION_CANCEL.toString());
    }

    @Override
    public EventResponse process(String rawXml) throws JAXBException {
        SubscriptionCancelEvent subscriptionEvent = xmlService.toObject(rawXml, SubscriptionCancelEvent.class);
        Subscription chancelledSubscription = subscriptionService.cancel(subscriptionEvent);
        //
        EventResponse result = new EventResponse();
        result.setSuccess(true);
        result.setAccountIdentifier(chancelledSubscription.getAccountId());
        result.setMessage("Subscription cancelled");

        log.info("Subscription cancelled: {}", result);
        return result;
    }
}
