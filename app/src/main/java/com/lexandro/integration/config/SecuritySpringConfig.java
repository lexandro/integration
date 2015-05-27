package com.lexandro.integration.config;

import com.lexandro.integration.api.v1.SubscriptionController;
import com.lexandro.integration.api.v1.UserController;
import com.lexandro.integration.security.ApplicationOAuthProviderProcessingFilter;
import lombok.extern.slf4j.Slf4j;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.oauth.common.signature.SharedConsumerSecretImpl;
import org.springframework.security.oauth.provider.BaseConsumerDetails;
import org.springframework.security.oauth.provider.ConsumerDetailsService;
import org.springframework.security.oauth.provider.InMemoryConsumerDetailsService;
import org.springframework.security.oauth.provider.filter.OAuthProviderProcessingFilter;
import org.springframework.security.oauth.provider.filter.ProtectedResourceProcessingFilter;
import org.springframework.security.oauth.provider.token.InMemoryProviderTokenServices;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenServices;
import org.springframework.security.openid.OpenIDAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;


@Configuration
@EnableWebMvcSecurity
@Slf4j
public class SecuritySpringConfig extends WebSecurityConfigurerAdapter {

    @Value("${appdirect.consumer.key}")
    private String consumerKey;

    @Value("${appdirect.consumer.secret}")
    private String consumerSecret;

    @Resource
    private AuthenticationUserDetailsService authenticationUserDetailsService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();
        // Enable endpoints access
        httpSecurity
                .authorizeRequests()
                .antMatchers(SubscriptionController.BASE_PATH + "**", UserController.BASE_PATH + "**")
                .permitAll()
                .anyRequest()
                .authenticated();
//        // secure app urls
        httpSecurity
                .authorizeRequests()
                .antMatchers("/login/", "/loginpage")
                .permitAll()
                .antMatchers("/imaginarium/hello", "/logoutpage")
                .access("hasRole('ROLE_USER')")
                .and()
                .openidLogin()
                .authenticationUserDetailsService(authenticationUserDetailsService)

//                .loginProcessingUrl("/login")
//                .loginPage("/loginpage")
                .defaultSuccessUrl("/imaginarium/hello")
        ;


        //
        httpSecurity.addFilterAfter(oauthProviderProcessingFilter(), OpenIDAuthenticationFilter.class);
    }


    @Bean
    OAuthProviderProcessingFilter oauthProviderProcessingFilter() {
        List<RequestMatcher> requestMatchers = Arrays.asList(
                new AntPathRequestMatcher("/subscription/**"),
                new AntPathRequestMatcher("/users/**")
        );
        //
        ProtectedResourceProcessingFilter filter = new ApplicationOAuthProviderProcessingFilter(requestMatchers);
        //
        filter.setConsumerDetailsService(consumerDetailsService());
        filter.setTokenServices(providerTokenServices());
        //
        return filter;
    }

    @Bean
    public ConsumerDetailsService consumerDetailsService() {
        InMemoryConsumerDetailsService consumerDetailsService = new InMemoryConsumerDetailsService();

        BaseConsumerDetails consumerDetails = new BaseConsumerDetails();
        consumerDetails.setConsumerKey(consumerKey);
        consumerDetails.setSignatureSecret(new SharedConsumerSecretImpl(consumerSecret));
        consumerDetails.setRequiredToObtainAuthenticatedToken(false);

        Map<String, BaseConsumerDetails> consumerDetailsStore = new HashMap<>();
        consumerDetailsStore.put(consumerKey, consumerDetails);

        consumerDetailsService.setConsumerDetailsStore(consumerDetailsStore);

        return consumerDetailsService;
    }

    @Bean
    public OAuthProviderTokenServices providerTokenServices() {
        return new InMemoryProviderTokenServices();
    }

    @Bean
    public OAuthConsumer oAuthConsumer() {
        return new DefaultOAuthConsumer(consumerKey, consumerSecret);
    }
}
