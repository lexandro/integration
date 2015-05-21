package com.lexandro.integration.security;

import org.springframework.security.oauth.provider.filter.ProtectedResourceProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ApplicationOAuthProviderProcessingFilter extends ProtectedResourceProcessingFilter {

    private List<RequestMatcher> requestMatchers;

    public ApplicationOAuthProviderProcessingFilter(List<RequestMatcher> requestMatchers) {
        this.requestMatchers = requestMatchers;
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        return requestMatchers != null &&
                requestMatchers
                        .stream()
                        .filter(rm -> rm.matches(request))
                        .findFirst()
                        .isPresent();
    }
}
