package com.lexandro.integration.service.subscription;

import com.lexandro.integration.model.*;

public interface SubscriptionService {

    Subscription findByAccountId(String accountId);

    Subscription create(SubscriptionCreateEvent subscriptionCreateEvent);

    Subscription change(SubscriptionChangeEvent subscriptionChangeEvent);

    Subscription cancel(SubscriptionCancelEvent subscriptionCancelEvent);

    Subscription notice(SubscriptionNoticeEvent subscriptionNoticeEvent);


}
