package com.lexandro.integration.service.user;

import com.lexandro.integration.model.*;

import java.util.List;

public interface UserService {

    ApplicationUser findByOpenId(String openId);

    List<ApplicationUser> findByAccountId(String accountId);

    ApplicationUser assign(AssignUserEvent userEvent);

    ApplicationUser assign(Account account, User user);

    ApplicationUser unAssign(UnAssignUserEvent userEvent);

    void unAssignAll(String accountIdentifier);
}
