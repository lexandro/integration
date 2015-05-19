package com.lexandro.integration.acceptance.config;

import com.lexandro.integration.config.SpringConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;

@Import({SpringConfig.class})
@Configuration
@EnableAutoConfiguration
@ComponentScan("com.lexandro.integration")
public class CucumberSpringConfig {

//    public static void main(String[] args) {
//        SpringApplication.run(CucumberSpringConfig.class, args);
//    }

    @Resource
    protected WebApplicationContext context;


    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    @Bean
    public MockMvc mvc() {
        return MockMvcBuilders.webAppContextSetup(this.context).build();

    }
}