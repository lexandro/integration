package com.lexandro.integration.service.subscription;

import com.lexandro.integration.model.*;
import com.lexandro.integration.repository.SubscriptionRepository;
import com.lexandro.integration.service.exception.AccountMissingException;
import com.lexandro.integration.service.exception.UserExistsException;
import com.lexandro.integration.service.user.UserService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AppDirectSubscriptionServiceTest {

    public static final String TEST_ACCOUNT_ID = "testAccountId";
    public static final String NON_EXISTING_ACCOUNT_ID = "NON_EXISTING_ACCOUNT_ID";
    public static final String SUBSCRIPTION_ID = "subscriptionId";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    //
    @InjectMocks
    private AppDirectSubscriptionService appDirectSubscriptionService;
    @Mock
    private UserService userService;
    @Mock
    private SubscriptionRepository subscriptionRepository;

    private SubscriptionCreateEvent subscriptionCreateEvent;
    private SubscriptionChangeEvent subscriptionChangeEvent;
    private SubscriptionCancelEvent subscriptionCancelEvent;
    private SubscriptionNoticeEvent subscriptionNoticeEvent;
    private User creator;
    private Payload payload;
    private Company company;
    private Account account;
    private Subscription subcription;
    private Subscription savedSubcription;


    @Before
    public void setUp() throws Exception {
        appDirectSubscriptionService = new AppDirectSubscriptionService();
        initMocks(this);
        //
        subscriptionCreateEvent = new SubscriptionCreateEvent();
        subscriptionChangeEvent = new SubscriptionChangeEvent();
        subscriptionCancelEvent = new SubscriptionCancelEvent();
        subscriptionNoticeEvent = new SubscriptionNoticeEvent();
        //
        creator = new User();
        payload = new Payload();
        account = new Account();
        company = new Company();
        //
        creator.setUuid(TEST_ACCOUNT_ID);
        //
        account.setAccountIdentifier(TEST_ACCOUNT_ID);
        //
        payload.setCompany(company);
        payload.setAccount(account);
        //
        subscriptionCreateEvent.setCreator(creator);
        subscriptionCreateEvent.setPayload(payload);
        //
        subscriptionChangeEvent.setPayload(payload);
        //
        subscriptionCancelEvent.setPayload(payload);
        //
        subscriptionNoticeEvent.setPayload(payload);
        //
        subcription = new Subscription();
        subcription.setId(SUBSCRIPTION_ID);
        savedSubcription = new Subscription();
        //
        when(subscriptionRepository.findByAccountId(TEST_ACCOUNT_ID)).thenReturn(subcription);
        when(subscriptionRepository.save(subcription)).thenReturn(savedSubcription);

    }

    @Test
    public void shouldFindByAccountIdReadsFromRepository() throws Exception {
        // when
        appDirectSubscriptionService.findByAccountId("testAccountId");
        // then
        verify(subscriptionRepository).findByAccountId(TEST_ACCOUNT_ID);
    }

    /*
    findByAccountId
     */
    @Test
    public void shouldFindByAccountIdNotAcceptEmptyId() throws Exception {
        // given
        expectedException.expect(IllegalArgumentException.class);
        // when
        appDirectSubscriptionService.findByAccountId("");
    }

    @Test
    public void shouldFindByAccountIdNotAcceptNull() throws Exception {
        // given
        expectedException.expect(IllegalArgumentException.class);
        // when
        appDirectSubscriptionService.findByAccountId(null);
    }

    /*
    create
     */

    @Test
    public void shouldCreateLookupForAccountIdCandidate() throws Exception {
        // given
        subscriptionCreateEvent.getCreator().setUuid(NON_EXISTING_ACCOUNT_ID);
        // when
        appDirectSubscriptionService.create(subscriptionCreateEvent);
        // then
        verify(subscriptionRepository).findByAccountId(NON_EXISTING_ACCOUNT_ID);
    }

    @Test
    public void shouldCreateSaveNewSubscription() throws Exception {
        // given
        when(subscriptionRepository.findByAccountId(TEST_ACCOUNT_ID)).thenReturn(null);
        // when
        appDirectSubscriptionService.create(subscriptionCreateEvent);
        // then
        // FIXME gently skipping the subscription creation. Shame on me :)
        verify(subscriptionRepository).save(any(Subscription.class));
    }

    @Test
    public void shouldCreateAssignAdminAsUser() throws Exception {
        // given
        when(subscriptionRepository.findByAccountId(TEST_ACCOUNT_ID)).thenReturn(null);
        // when
        appDirectSubscriptionService.create(subscriptionCreateEvent);
        // then
        verify(userService).assign(any(Account.class), eq(creator));
    }

    @Test
    public void shouldCreateThrowUserExistsExcepionIfAlreadyStored() throws Exception {
        // given
        when(subscriptionRepository.findByAccountId(TEST_ACCOUNT_ID)).thenReturn(subcription);
        expectedException.expect(UserExistsException.class);
        expectedException.expectMessage(TEST_ACCOUNT_ID);
        // when
        appDirectSubscriptionService.create(subscriptionCreateEvent);
    }

    @Test
    public void shouldCreateNotAcceptNullEvent() throws Exception {
        // given
        expectedException.expect(IllegalArgumentException.class);
        // when
        appDirectSubscriptionService.create(null);
    }

    /*
    change
     */

    @Test
    public void shouldChangeLookUpForAccountIdIndb() throws Exception {
        // when
        appDirectSubscriptionService.change(subscriptionChangeEvent);
        // then
        verify(subscriptionRepository).findByAccountId(TEST_ACCOUNT_ID);
    }

    @Test
    public void shouldChangeOverwriteSubscription() throws Exception {
        // when
        appDirectSubscriptionService.change(subscriptionChangeEvent);
        // then
        verify(subscriptionRepository).save(subcription);
    }

    @Test
    public void shouldChangeThrowAccountMissingExceptionIfNoSubscriptionFound() throws Exception {
        // given
        subscriptionChangeEvent.getPayload().getAccount().setAccountIdentifier(NON_EXISTING_ACCOUNT_ID);
        expectedException.expect(AccountMissingException.class);
        expectedException.expectMessage(NON_EXISTING_ACCOUNT_ID);
        // when
        appDirectSubscriptionService.change(subscriptionChangeEvent);
    }

    @Test
    public void shouldChangeReturnSavedSubscription() throws Exception {
        // when
        Subscription actualResult = appDirectSubscriptionService.change(subscriptionChangeEvent);
        // then
        assertEquals(savedSubcription, actualResult);
    }

    @Test
    public void shouldChangeNotAcceptNullEvent() throws Exception {
        // given
        expectedException.expect(IllegalArgumentException.class);
        // when
        appDirectSubscriptionService.change(null);
    }

    /*
    cancel
    */

    @Test
    public void shouldCancelLookUpForAccountIdIndb() throws Exception {
        // when
        appDirectSubscriptionService.cancel(subscriptionCancelEvent);
        // then
        verify(subscriptionRepository).findByAccountId(TEST_ACCOUNT_ID);
    }

    @Test
    public void shouldCancelUnassignUsersBeforeDeletion() throws Exception {
        // when
        appDirectSubscriptionService.cancel(subscriptionCancelEvent);
        // then
        verify(userService).unAssignAll(TEST_ACCOUNT_ID);
    }

    @Test
    public void shouldCancelDeleteSubscriptionById() throws Exception {
        // given
        // when
        appDirectSubscriptionService.cancel(subscriptionCancelEvent);
        // then
        verify(subscriptionRepository).delete(SUBSCRIPTION_ID);
    }

    @Test
    public void shouldCancelThrowAccountMissingExceptionIfNoSubscriptionFound() throws Exception {
        // given
        subscriptionCancelEvent.getPayload().getAccount().setAccountIdentifier(NON_EXISTING_ACCOUNT_ID);
        expectedException.expect(AccountMissingException.class);
        expectedException.expectMessage(NON_EXISTING_ACCOUNT_ID);
        // when
        appDirectSubscriptionService.cancel(subscriptionCancelEvent);
    }

    @Test
    public void shouldCancelNotAcceptNullEvent() throws Exception {
        // given
        expectedException.expect(IllegalArgumentException.class);
        // when
        appDirectSubscriptionService.cancel(null);
    }

    /*
    notice
     */

    @Test
    public void shouldNoticeLookUpForAccountIdIndb() throws Exception {
        // when
        appDirectSubscriptionService.notice(subscriptionNoticeEvent);
        // then
        verify(subscriptionRepository).findByAccountId(TEST_ACCOUNT_ID);
    }

    @Test
    public void shouldNoticeOverrideSubscriptionWithTheNewStatus() throws Exception {
        // when
        appDirectSubscriptionService.notice(subscriptionNoticeEvent);
        // then
        verify(subscriptionRepository).save(subcription);
    }

    @Test
    public void shouldNoticeReturnSavedSubscription() throws Exception {
        // when
        Subscription actualResult = appDirectSubscriptionService.notice(subscriptionNoticeEvent);
        // then
        assertEquals(savedSubcription, actualResult);
    }


    @Test
    public void shouldNoticeThrowAccountMissingExceptionIfNoSubscriptionFound() throws Exception {
        // given
        subscriptionNoticeEvent.getPayload().getAccount().setAccountIdentifier(NON_EXISTING_ACCOUNT_ID);
        expectedException.expect(AccountMissingException.class);
        expectedException.expectMessage(NON_EXISTING_ACCOUNT_ID);
        // when
        appDirectSubscriptionService.notice(subscriptionNoticeEvent);
    }

    @Test
    public void shouldNoticeNotAcceptNullEvent() throws Exception {
        // given
        expectedException.expect(IllegalArgumentException.class);
        // when
        appDirectSubscriptionService.notice(null);
    }

}