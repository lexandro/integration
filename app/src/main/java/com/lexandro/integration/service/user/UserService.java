package com.lexandro.integration.service.user;

import com.lexandro.integration.model.*;

public interface UserService {
    ApplicationUser assign(AssignUserEvent userEvent);

    ApplicationUser assign(Account account, User user);

    ApplicationUser unAssign(UnAssignUserEvent userEvent);
}
