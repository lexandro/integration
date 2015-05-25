package com.lexandro.integration.api.v1;

import com.lexandro.integration.model.ApplicationUser;
import com.lexandro.integration.model.Subscription;
import com.lexandro.integration.service.subscription.SubscriptionService;
import com.lexandro.integration.service.user.UserService;
import com.wordnik.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.security.Principal;

import static com.lexandro.integration.api.v1.ApiVersion.VERSION;

@Controller
@RequestMapping(value = AuthenticationController.BASE_PATH)
@Api(value = "Login-" + VERSION, description = "Basic API version for Logging in into Imaginarium")
@Slf4j
public class AuthenticationController {

    protected static final String BASE_PATH = "/";

    @Resource
    private SubscriptionService subscriptionService;

    @Resource
    private UserService userService;


    @RequestMapping(value = "/loginpage", method = RequestMethod.GET)
    public String doLogin(Principal principal) {
        log.debug("Logged in as {}", principal);
        return "login";
    }

    @RequestMapping(value = "/dologout", method = RequestMethod.GET)
    public String doLogout(Principal principal) {
        Assert.notNull(principal);
        ApplicationUser applicationUser = userService.findByOpenId(principal.getName());
        Subscription subscription = subscriptionService.findByAccountId(applicationUser.getAccountId());
        String logoutUrl = "login";
        // we can redirect to app market logout url only when the user and the subscription is valid
        if (applicationUser != null && subscription != null) {
            logoutUrl = "redirect:" + subscription.getMarketplace().getBaseUrl() + "/applogout?openid=" + principal.getName();
            //
            log.info("Logging out with: {}", logoutUrl);
        }
        //
        return logoutUrl;
    }
}
