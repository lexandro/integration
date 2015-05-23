package com.lexandro.integration.service.subscription;

import com.lexandro.integration.model.*;

public interface SubscriptionService {

    Subscription create(SubscriptionCreateEvent subscriptionCreateEvent);

    Subscription change(SubscriptionChangeEvent subscriptionChangeEvent);

    Subscription cancel(SubscriptionCancelEvent subscriptionCancelEvent);

    Subscription status(SubscriptionNoticeEvent subscriptionNoticeEvent);
}
