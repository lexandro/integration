package com.lexandro.integration.service.user;

import com.lexandro.integration.model.*;
import com.lexandro.integration.repository.ApplicationUserRepository;
import com.lexandro.integration.repository.SubscriptionRepository;
import com.lexandro.integration.service.exception.AccountMissingException;
import com.lexandro.integration.service.exception.UserExistsException;
import com.lexandro.integration.service.exception.UserMissingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AppDirectUserServiceTest {

    public static final String TEST_ACCOUNT_ID = "testAccountId";
    public static final String TEST_OPEN_ID = "testOpenId";
    public static final String TEST_APPUSER_ID = "TEST_APPUSER_ID";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @InjectMocks
    private AppDirectUserService appDirectUserService;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private ApplicationUserRepository applicationUserRepository;
    //
    private User user;
    private Payload payload;

    private Account account;
    private Subscription subscription;
    private ApplicationUser applicationUser;
    private UnAssignUserEvent unAssignUserEvent;


    @Before
    public void setUp() throws Exception {
        appDirectUserService = new AppDirectUserService();
        initMocks(this);
        //
        user = new User();
        account = new Account();
        subscription = new Subscription();
        applicationUser = new ApplicationUser();
        payload = new Payload();
        unAssignUserEvent = new UnAssignUserEvent();
        //
        user.setOpenId(TEST_OPEN_ID);
        account.setAccountIdentifier(TEST_ACCOUNT_ID);
        payload.setAccount(account);
        payload.setUser(user);
        unAssignUserEvent.setPayload(payload);
        applicationUser.setId(TEST_APPUSER_ID);
        //
        when(subscriptionRepository.findByAccountId(TEST_ACCOUNT_ID)).thenReturn(subscription);
        //
    }
    /*
    findByOpenId
     */

    @Test
    public void shouldFindByOpenIdReadsUserFromTheDatabase() throws Exception {
        // when
        appDirectUserService.findByOpenId(TEST_OPEN_ID);
        // then
        verify(applicationUserRepository).findByOpenId(TEST_OPEN_ID);
    }

    @Test
    public void shouldFindByOpenIdNotAcceptingEmptyId() throws Exception {
        // given
        expectedException.expect(IllegalArgumentException.class);
        // when
        appDirectUserService.findByOpenId("");
    }

    @Test
    public void shouldFindByOpenIdNotAcceptingNullId() throws Exception {
        // given
        expectedException.expect(IllegalArgumentException.class);
        // when
        appDirectUserService.findByOpenId("");
    }
     /*
    findByAccountId
     */

    @Test
    public void shouldFindByAccountIdReadsUserFromTheDatabase() throws Exception {
        // when
        appDirectUserService.findByAccountId(TEST_ACCOUNT_ID);
        // then
        verify(applicationUserRepository).findByAccountId(TEST_ACCOUNT_ID);
    }

    @Test
    public void shouldFindByAccountNotAcceptingEmptyId() throws Exception {
        // given
        expectedException.expect(IllegalArgumentException.class);
        // when
        appDirectUserService.findByAccountId("");
    }

    @Test
    public void shouldFindByAccountNotAcceptingNullId() throws Exception {
        // given
        expectedException.expect(IllegalArgumentException.class);
        // when
        appDirectUserService.findByAccountId("");
    }
    /*
    assign(event)
     */

    @Test
    public void shouldAssignWithEventNotAcceptingNull() throws Exception {
        // given
        expectedException.expect(IllegalArgumentException.class);
        // when
        appDirectUserService.assign(null);
    }

   /*
    assign(Account, User)
     */

    @Test
    public void shouldAssignWithAccAndUserNotAcceptingNullAccount() throws Exception {
        // given
        expectedException.expect(IllegalArgumentException.class);
        // when
        appDirectUserService.assign(null, user);
    }

    @Test
    public void shouldAssignWithAccAndUserNotAcceptingNullUser() throws Exception {
        // given
        expectedException.expect(IllegalArgumentException.class);
        // when
        appDirectUserService.assign(account, null);
    }

    @Test
    public void shouldAssignWithAccAndUserNotAcceptingNullParams() throws Exception {
        // given
        expectedException.expect(IllegalArgumentException.class);
        // when
        appDirectUserService.assign(null, null);
    }

    @Test
    public void shouldAssignWithAccAndUserThrowingAccountMissingExceptionForNonExistingSubscription() throws Exception {
        // given
        expectedException.expect(AccountMissingException.class);
        when(subscriptionRepository.findByAccountId(TEST_ACCOUNT_ID)).thenReturn(null);
        // when
        appDirectUserService.assign(account, user);
    }

    @Test
    public void shouldAssignWithAccAndUserTriesToLoadAppUser() throws Exception {
        // when
        appDirectUserService.assign(account, user);
        // then
        verify(applicationUserRepository).findByAccountIdAndOpenId(TEST_ACCOUNT_ID, TEST_OPEN_ID);
    }

    @Test
    public void shouldAssignWithAccAndUserSavesNewUserIfNoUserExisting() throws Exception {
        // when
        appDirectUserService.assign(account, user);
        // then
        verify(applicationUserRepository).save(any(ApplicationUser.class));
    }

    @Test
    public void shouldAssignWithAccAndUserThrowingExceptionIfUserAlreadyExists() throws Exception {
        // given
        expectedException.expect(UserExistsException.class);
        expectedException.expectMessage("User exists with Id: " + TEST_OPEN_ID);
        when(applicationUserRepository.findByAccountIdAndOpenId(TEST_ACCOUNT_ID, TEST_OPEN_ID)).thenReturn(applicationUser);
        // when
        appDirectUserService.assign(account, user);
        // then
        verify(applicationUserRepository).save(any(ApplicationUser.class));
    }
    /*
    unassign
     */

    @Test
    public void shouldUnassignNotAcceptingNullArgument() throws Exception {
        // given
        expectedException.expect(IllegalArgumentException.class);
        // when
        appDirectUserService.unAssign(null);
    }

    @Test
    public void shouldUnassignLoadsUserDataByAccIdAndUserId() throws Exception {
        // given
        when(applicationUserRepository.findByAccountIdAndOpenId(TEST_ACCOUNT_ID, TEST_OPEN_ID)).thenReturn(applicationUser);
        // when
        appDirectUserService.unAssign(unAssignUserEvent);
        // then
        verify(applicationUserRepository).findByAccountIdAndOpenId(TEST_ACCOUNT_ID, TEST_OPEN_ID);
    }

    @Test
    public void shouldUnassignDeletesLoadedUserById() throws Exception {
        // given
        when(applicationUserRepository.findByAccountIdAndOpenId(TEST_ACCOUNT_ID, TEST_OPEN_ID)).thenReturn(applicationUser);
        // when
        appDirectUserService.unAssign(unAssignUserEvent);
        // then
        verify(applicationUserRepository).delete(TEST_APPUSER_ID);
    }


    @Test
    public void shouldUnassignThrowsUserMissingExceptionIfNoUserInDB() throws Exception {
        // given
        expectedException.expect(UserMissingException.class);
        expectedException.expectMessage("User missing  with Id: " + TEST_OPEN_ID);
        // when
        appDirectUserService.unAssign(unAssignUserEvent);
    }

    /*
    unassignAll
    */
    @Test
    public void shouldUnAssignAllCallRepoDelete() throws Exception {
        // when
        appDirectUserService.unAssignAll(TEST_ACCOUNT_ID);
        // then
        verify(applicationUserRepository).deleteByAccountId(TEST_ACCOUNT_ID);
    }

    @Test
    public void shouldUnAssignAllNotAcceptingEmptyId() throws Exception {
        // given
        expectedException.expect(IllegalArgumentException.class);
        // when
        appDirectUserService.unAssignAll("");
    }

    @Test
    public void shouldUnAssignAllNotAcceptingNullId() throws Exception {
        // given
        expectedException.expect(IllegalArgumentException.class);
        // when
        appDirectUserService.unAssignAll(null);
    }
}
