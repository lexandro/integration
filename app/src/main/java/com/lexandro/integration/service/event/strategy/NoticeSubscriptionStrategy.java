package com.lexandro.integration.service.event.strategy;

import com.lexandro.integration.model.EventResponse;
import com.lexandro.integration.model.EventType;
import com.lexandro.integration.model.Subscription;
import com.lexandro.integration.model.SubscriptionNoticeEvent;
import com.lexandro.integration.service.subscription.SubscriptionService;
import com.lexandro.integration.service.xml.XmlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

@Service
@Slf4j
public class NoticeSubscriptionStrategy implements EventProcessorStrategy {

    @Resource
    private XmlService xmlService;

    @Resource
    private SubscriptionService subscriptionService;

    @Override
    public Boolean apply(String xmlString) {
        return xmlString != null && xmlString.contains(EventType.SUBSCRIPTION_NOTICE.toString());
    }

    @Override
    public EventResponse process(String rawXml) throws JAXBException {
        SubscriptionNoticeEvent subscriptionEvent = xmlService.toObject(rawXml, SubscriptionNoticeEvent.class);
        Subscription noticedSubscription = subscriptionService.notice(subscriptionEvent);
        //
        EventResponse result = new EventResponse();
        result.setSuccess(true);
        result.setAccountIdentifier(noticedSubscription.getAccountId());
        result.setMessage("Subscription status changed");

        log.info("Subscription status changed: {}", result);
        return result;
    }
}
