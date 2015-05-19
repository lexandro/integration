package com.lexandro.integration.acceptance.stepdefs;

import com.lexandro.integration.acceptance.config.CucumberSpringConfig;
import flexjson.JSONSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;


@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@WebAppConfiguration
@ContextConfiguration(classes = {CucumberSpringConfig.class})
public class AbstractStepDefs {

    @Resource
    protected WebApplicationContext context;


    //
    @Resource
    protected MockMvc mvc;

    protected String toJson(Object data) {
        return new JSONSerializer().exclude("*.class").prettyPrint(true).serialize(data);
    }


}

