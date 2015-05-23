package com.lexandro.integration.service.user;

import com.lexandro.integration.model.ApplicationUser;
import com.lexandro.integration.model.AssignUserEvent;
import com.lexandro.integration.model.UnAssignUserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AppDirectUserService implements UserService {
    @Override
    public ApplicationUser assign(AssignUserEvent userEvent) {
        log.info("Assign called {}", userEvent);
        ApplicationUser result = new ApplicationUser();
        result.setId("1");
        return result;
    }

    @Override
    public ApplicationUser unAssign(UnAssignUserEvent userEvent) {
        log.info("unAssign called {}", userEvent);
        ApplicationUser result = new ApplicationUser();
        result.setId("1");
        return result;
    }
}
