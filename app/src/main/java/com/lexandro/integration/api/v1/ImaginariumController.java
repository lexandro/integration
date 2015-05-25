package com.lexandro.integration.api.v1;

import com.lexandro.integration.model.ApplicationUser;
import com.lexandro.integration.model.Subscription;
import com.lexandro.integration.service.subscription.SubscriptionService;
import com.lexandro.integration.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(value = ImaginariumController.BASE_PATH)
@Slf4j
public class ImaginariumController {

    protected static final String BASE_PATH = "/imaginarium/";

    @Resource
    private SubscriptionService subscriptionService;

    @Resource
    private UserService userService;

    @RequestMapping(value = "hello", method = RequestMethod.GET)
    public String sayHello(Principal principal, Model model) {
        Assert.notNull(principal);
        ApplicationUser loggedInUser = userService.findByOpenId(principal.getName());
        String accountId = loggedInUser.getAccountId();
        //
        Subscription subscription = subscriptionService.findByAccountId(accountId);
        List<ApplicationUser> applicationUserList = userService.findByAccountId(accountId);
        //
        model.addAttribute("subscription", subscription);
        model.addAttribute("user", loggedInUser);
        model.addAttribute("applicationUserList", applicationUserList);


        log.info("princ: {}", principal);
        log.info("princName: {}", principal.getName());
        System.out.println("Hello");


        return "hello";
    }
}
