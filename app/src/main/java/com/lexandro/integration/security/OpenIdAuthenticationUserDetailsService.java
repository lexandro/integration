package com.lexandro.integration.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.singletonList;

@Service
@Slf4j
public class OpenIdAuthenticationUserDetailsService implements AuthenticationUserDetailsService {

    @Override
    public UserDetails loadUserDetails(Authentication authentication) throws UsernameNotFoundException {
        log.info("Authenticating {}", authentication);
        GrantedAuthority userRole = new SimpleGrantedAuthority("ROLE_USER");
        return new User((String) authentication.getPrincipal(), "password", singletonList(userRole));
    }
}
