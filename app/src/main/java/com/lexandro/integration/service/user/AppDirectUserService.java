package com.lexandro.integration.service.user;

import com.lexandro.integration.model.*;
import com.lexandro.integration.repository.ApplicationUserRepository;
import com.lexandro.integration.repository.SubscriptionRepository;
import com.lexandro.integration.service.exception.AccountMissingException;
import com.lexandro.integration.service.exception.UserExistsException;
import com.lexandro.integration.service.exception.UserMissingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class AppDirectUserService implements UserService {


    @Resource
    private SubscriptionRepository subscriptionRepository;

    @Resource
    private ApplicationUserRepository applicationUserRepository;


    @Override
    public ApplicationUser assign(AssignUserEvent userEvent) {
        log.info("Assign called {}", userEvent);
        //
        Payload payload = userEvent.getPayload();

        String accountIdentifier = payload.getAccount().getAccountIdentifier();
        checkSubscription(accountIdentifier);
        //
        String userId = payload.getUser().getOpenId();
        ApplicationUser appUser = applicationUserRepository.findByAccountIdAndOpenId(accountIdentifier, userId);
        if (appUser == null) {
            //
            User user = payload.getUser();
            appUser = ApplicationUser
                    .builder()
                    .accountId(payload.getAccount().getAccountIdentifier())
                    .openId(user.getOpenId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .language(user.getLanguage())
                    .userEmail(user.getEmail())
                    .uuid(user.getUuid())
                    .attributes(user.getAttributes())
                    .build();
            //
            applicationUserRepository.save(appUser);
        } else {
            throw new UserExistsException(userId);
        }
        return appUser;
    }

    @Override
    public ApplicationUser unAssign(UnAssignUserEvent userEvent) {
        log.info("unAssign called {}", userEvent);
        //
        Payload payload = userEvent.getPayload();
        String accountIdentifier = payload.getAccount().getAccountIdentifier();
        checkSubscription(accountIdentifier);
        //
        String userId = payload.getUser().getOpenId();
        ApplicationUser appUser = applicationUserRepository.findByAccountIdAndOpenId(accountIdentifier, userId);

        if (appUser != null) {
            applicationUserRepository.delete(appUser.getId());
        } else {
            throw new UserMissingException(userId);
        }
        return appUser;
    }

    private void checkSubscription(String accountIdentifier) {
        Subscription subscription = subscriptionRepository.findByAccountId(accountIdentifier);
        // are we subscribing to an existing subscription?
        if (subscription == null) {
            throw new AccountMissingException(accountIdentifier);
        }
    }
}
