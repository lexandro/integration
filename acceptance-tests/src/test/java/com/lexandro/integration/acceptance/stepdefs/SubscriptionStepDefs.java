package com.lexandro.integration.acceptance.stepdefs;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.lexandro.integration.acceptance.data.CucumberData.resultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Slf4j
public class SubscriptionStepDefs extends AbstractStepDefs {

    @Given("^we have a subscription create event$")
    public void we_have_a_subscription_create_event() throws Throwable {
    }


    @When("^I call create subscription endpoint version (\\d+)$")
    public void I_call_create_subscription_endpoint(int version) throws Throwable {
        MockHttpServletRequestBuilder createSubscriptionMessage = get("/subscription/v" + version + "/create/a").contentType(MediaType.APPLICATION_XML);
        resultActions = mvc.perform(createSubscriptionMessage);
    }

}
