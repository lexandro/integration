package com.lexandro.integration.service.subscription;

import com.lexandro.integration.model.*;
import com.lexandro.integration.repository.SubscriptionRepository;
import com.lexandro.integration.service.exception.AccountMissingException;
import com.lexandro.integration.service.exception.UserExistsException;
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
        String accountIdCandidate = subscriptionCreateEvent.getCreator().getUuid();
        Subscription subscription = subscriptionRepository.findByAccountId(accountIdCandidate);
        //
        // FIXME temp avoidance of dup user error! SHOULD REMOVED!
        subscription = null;
        if (subscription == null) {
            subscription = Subscription.builder()
                    .accountId(subscriptionCreateEvent.getPayload().getCompany().getUuid())
                    .marketplace(subscriptionCreateEvent.getMarketplace())
                    .creator(subscriptionCreateEvent.getCreator())
                    .company(subscriptionCreateEvent.getPayload().getCompany())
                    .order(subscriptionCreateEvent.getPayload().getOrder())
                    .build();
            //
            subscriptionRepository.save(subscription);
            return subscription;
        } else {
            log.error("CreateSubscription - subscription exists with following UUID: {}", subscription);
            throw new UserExistsException(accountIdCandidate);
        }
    }

    @Override
    public Subscription change(SubscriptionChangeEvent subscriptionChangeEvent) {
        log.debug("Subscriptionservice change with {}", subscriptionChangeEvent);

        String accountIdentifier = subscriptionChangeEvent.getPayload().getAccount().getAccountIdentifier();
        Subscription subscription = subscriptionRepository.findByAccountId(accountIdentifier);
        //
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
            log.error("ChangeSubscription - account missing: {}", subscriptionChangeEvent.getCreator());
            throw new AccountMissingException(accountIdentifier);
        }
    }

    @Override
    public Subscription cancel(SubscriptionCancelEvent subscriptionCancelEvent) {
        log.debug("Subscriptionservice cancel with {}", subscriptionCancelEvent);

        String accountIdentifier = subscriptionCancelEvent.getPayload().getAccount().getAccountIdentifier();
        Subscription subscription = subscriptionRepository.findByAccountId(accountIdentifier);
        //
        if (subscription != null) {
            // Picked delete by intention to it keep simple. Other option to "deactivate" the account
            subscriptionRepository.delete(subscription.getId());

            return subscription;
        } else {
            log.error("CancelSubscription - account missing: {}", subscriptionCancelEvent.getCreator());
            throw new AccountMissingException(accountIdentifier);
        }

    }

    @Override
    public Subscription notice(SubscriptionNoticeEvent subscriptionNoticeEvent) {
        log.debug("Subscriptionservice notice with {}", subscriptionNoticeEvent);
        Subscription subscription = subscriptionRepository.findByAccountId(subscriptionNoticeEvent.getPayload().getAccount().getAccountIdentifier());
        //
        if (subscription != null) {
            // Quite unlikely to change them
//            subscription.setMarketplace(subscriptionChangeEvent.getMarketplace());
//            subscription.setCreator(subscriptionChangeEvent.getCreator());
            // Notice is about to handle the global subscription status of the app, but I simplified to save the last notice
            subscription.setAccount(subscriptionNoticeEvent.getPayload().getAccount());
            subscription.setNotice(subscriptionNoticeEvent.getPayload().getNotice());
            return subscription;
        } else {
            log.error("NoticeSubscription - user missing: {}", subscriptionNoticeEvent.getCreator());
            throw new AccountMissingException(subscriptionNoticeEvent.getCreator().getUuid());
        }

    }


}
