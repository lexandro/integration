package com.lexandro.integration.service;

import com.lexandro.integration.model.*;
import com.lexandro.integration.repository.SubscriptionRepository;
import com.lexandro.integration.service.exception.UserExistsException;
import com.lexandro.integration.service.exception.UserMissingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class AppDirectSubscriptionService implements SubscriptionService {

    @Resource
    private SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription create(SubscriptionCreateEvent subscriptionCreateEvent) {
        log.debug("Subscriptionservice create with {}", subscriptionCreateEvent);
        //
        Subscription subscription = subscriptionRepository.findOne(subscriptionCreateEvent.getCreator().getUuid());
        //
        EventResponse result = null;

        // FIXME temp avoidance of dup user error! SHOULD REMOVED!
        subscription = null;
        if (subscription == null) {
            subscription = Subscription.builder()
                    .id(subscriptionCreateEvent.getCreator().getUuid())
                    .marketplace(subscriptionCreateEvent.getMarketplace())
                    .creator(subscriptionCreateEvent.getCreator())
                    .company(subscriptionCreateEvent.getPayload().getCompany())
                    .order(subscriptionCreateEvent.getPayload().getOrder())
                    .build();
            //
            subscriptionRepository.save(subscription);
            return subscription;
        } else {
            log.error("CreateSubscription - user exists: {}", subscription);
            throw new UserExistsException(subscription);
        }
    }

    @Override
    public Subscription change(SubscriptionChangeEvent subscriptionChangeEvent) {
        log.debug("Subscriptionservice change with {}", subscriptionChangeEvent);

        Subscription subscription = subscriptionRepository.findOne(subscriptionChangeEvent.getCreator().getUuid());
        //
        EventResponse result = null;

        if (subscription != null) {
            // Quite unlikely to change them
//            subscription.setMarketplace(subscriptionChangeEvent.getMarketplace());
//            subscription.setCreator(subscriptionChangeEvent.getCreator());
            subscription.setOrder(subscriptionChangeEvent.getPayload().getOrder());
            subscription.setAccount(subscriptionChangeEvent.getPayload().getAccount());
            //
            subscriptionRepository.save(subscription);
            return subscription;
        } else {
            log.error("Changesubscription - user missing: {}", subscriptionChangeEvent.getCreator());
            throw new UserMissingException(subscriptionChangeEvent.getCreator().getUuid());
        }
    }

    @Override
    public Subscription cancel(SubscriptionCancelEvent subscriptionCancelEvent) {
        return null;
    }

    @Override
    public Subscription status(SubscriptionNoticeEvent subscriptionNoticeEvent) {
        return null;
    }


}
