package com.lexandro.integration.service.subscription;

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
            log.error("ChangeSubscription - user missing: {}", subscriptionChangeEvent.getCreator());
            throw new UserMissingException(subscriptionChangeEvent.getCreator().getUuid());
        }
    }

    @Override
    public Subscription cancel(SubscriptionCancelEvent subscriptionCancelEvent) {
        log.debug("Subscriptionservice cancel with {}", subscriptionCancelEvent);

        Subscription subscription = subscriptionRepository.findOne(subscriptionCancelEvent.getCreator().getUuid());
        //
        EventResponse result = null;

        if (subscription != null) {
            // Picked delete by intention to it keep simple. Other option to "deactivate" the account
            subscriptionRepository.delete(subscription.getId());

            return subscription;
        } else {
            log.error("CancelSubscription - user missing: {}", subscriptionCancelEvent.getCreator());
            throw new UserMissingException(subscriptionCancelEvent.getCreator().getUuid());
        }

    }

    @Override
    public Subscription notice(SubscriptionNoticeEvent subscriptionNoticeEvent) {
        log.debug("Subscriptionservice notice with {}", subscriptionNoticeEvent);
        Subscription subscription = subscriptionRepository.findOne(subscriptionNoticeEvent.getCreator().getUuid());
        //
        EventResponse result = null;
        if (subscription != null) {
            // Quite unlikely to change them
//            subscription.setMarketplace(subscriptionChangeEvent.getMarketplace());
//            subscription.setCreator(subscriptionChangeEvent.getCreator());
            subscription.setAccount(subscriptionNoticeEvent.getPayload().getAccount());
            subscription.setNotice(subscriptionNoticeEvent.getPayload().getNotice());


            return subscription;
        } else {
            log.error("NoticeSubscription - user missing: {}", subscriptionNoticeEvent.getCreator());
            throw new UserMissingException(subscriptionNoticeEvent.getCreator().getUuid());
        }

    }


}
