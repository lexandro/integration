package com.lexandro.integration.service.user;

import com.lexandro.integration.model.ApplicationUser;
import com.lexandro.integration.model.UnAssignUserEvent;

public interface UserService {
    ApplicationUser assign(UnAssignUserEvent userEvent);

    ApplicationUser unAssign(UnAssignUserEvent userEvent);
}
