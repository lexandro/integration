package com.lexandro.integration.service.subscription;

import com.lexandro.integration.model.*;
import com.lexandro.integration.repository.SubscriptionRepository;
import com.lexandro.integration.service.exception.AccountMissingException;
import com.lexandro.integration.service.exception.UserExistsException;
import com.lexandro.integration.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class AppDirectSubscriptionService implements SubscriptionService {

    public static final String DUMMY_TOKEN_PATTERN = "token=dummyOrder";
    @Resource
    private UserService userService;
    @Resource
    private SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription create(SubscriptionCreateEvent subscriptionCreateEvent) {
        log.debug("Subscriptionservice create with {}", subscriptionCreateEvent);

        // Handling dummy calls
        String accountIdCandidate;
        log.debug("Subscriptionservice generate account for {}", subscriptionCreateEvent.getReturnUrl());
        String returnUrl = subscriptionCreateEvent.getReturnUrl();
        if (returnUrl == null || !returnUrl.contains(DUMMY_TOKEN_PATTERN)) {
            accountIdCandidate = subscriptionCreateEvent.getCreator().getUuid();
        } else {
            accountIdCandidate = "dummy-account";
        }

        Subscription subscription = subscriptionRepository.findByAccountId(accountIdCandidate);
        //
        // FIXME temp avoidance of dup user error! SHOULD REMOVED!
        subscription = null;
        if (subscription == null) {
            // Setting account active
            Account account = new Account();
            account.setAccountIdentifier(accountIdCandidate);
            account.setStatus(AccountStatus.ACTIVE);
            //
            subscription = Subscription.builder()
                    .accountId(accountIdCandidate)
                    .account(account)
                    .marketplace(subscriptionCreateEvent.getMarketplace())
                    .creator(subscriptionCreateEvent.getCreator())
                    .company(subscriptionCreateEvent.getPayload().getCompany())
                    .order(subscriptionCreateEvent.getPayload().getOrder())
                    .build();
            //
            subscriptionRepository.save(subscription);
            // adding admin user to the subscription
            userService.assign(account, subscriptionCreateEvent.getCreator());
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
        String accountIdentifier = subscriptionNoticeEvent.getPayload().getAccount().getAccountIdentifier();
        Subscription subscription = subscriptionRepository.findByAccountId(accountIdentifier);
        //
        if (subscription != null) {
            // Quite unlikely to change them
//            subscription.setMarketplace(subscriptionChangeEvent.getMarketplace());
//            subscription.setCreator(subscriptionChangeEvent.getCreator());
            // Notice is about to handle the global subscription status of the app, but I simplified to save the last notice
            subscription.setAccount(subscriptionNoticeEvent.getPayload().getAccount());
            subscription.setNotice(subscriptionNoticeEvent.getPayload().getNotice());
            subscription = subscriptionRepository.save(subscription);
            return subscription;
        } else {
            log.error("NoticeSubscription - account missing: {}", subscriptionNoticeEvent);
            throw new AccountMissingException(accountIdentifier);
        }

    }


}
