package com.lexandro.integration.service.user;

import com.lexandro.integration.model.*;
import com.lexandro.integration.repository.ApplicationUserRepository;
import com.lexandro.integration.repository.SubscriptionRepository;
import com.lexandro.integration.service.exception.AccountMissingException;
import com.lexandro.integration.service.exception.UserExistsException;
import com.lexandro.integration.service.exception.UserMissingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class AppDirectUserService implements UserService {

    @Resource
    private SubscriptionRepository subscriptionRepository;

    @Resource
    private ApplicationUserRepository applicationUserRepository;

    @Override
    public ApplicationUser findByOpenId(String openId) {
        Assert.hasText(openId);
        return applicationUserRepository.findByOpenId(openId);
    }

    @Override
    public List<ApplicationUser> findByAccountId(String findByAccountId) {
        Assert.hasText(findByAccountId);
        return applicationUserRepository.findByAccountId(findByAccountId);
    }

    @Override
    public ApplicationUser assign(AssignUserEvent userEvent) {
        Assert.notNull(userEvent);
        log.info("Assign called {}", userEvent);
        //
        Payload payload = userEvent.getPayload();
        return assign(payload.getAccount(), payload.getUser());
    }

    @Override
    public ApplicationUser assign(Account account, User user) {
        Assert.notNull(account);
        Assert.notNull(user);
        String accountIdentifier = account.getAccountIdentifier();
        checkSubscription(accountIdentifier);
        //
        String userId = user.getOpenId();
        ApplicationUser appUser = applicationUserRepository.findByAccountIdAndOpenId(accountIdentifier, userId);
        if (appUser == null) {
            //
            appUser = ApplicationUser
                    .builder()
                    .accountId(accountIdentifier)
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
        Assert.notNull(userEvent);
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

    @Override
    public void unAssignAll(String accountIdentifier) {
        Assert.hasText(accountIdentifier);
        log.info("unAssign all users for {}", accountIdentifier);
        //
        applicationUserRepository.deleteByAccountId(accountIdentifier);
    }

    private void checkSubscription(String accountIdentifier) {
        Subscription subscription = subscriptionRepository.findByAccountId(accountIdentifier);
        // are we subscribing to an existing subscription?
        if (subscription == null) {
            throw new AccountMissingException(accountIdentifier);
        }
    }
}
