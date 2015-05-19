package com.lexandro.integration.acceptance.stepdefs;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubscriptionStepDefs {

    @Given("^the app started$")
    public void the_app_started() throws Throwable {
        log.info("The app started");
    }

    @When("^I call create subscription endpoint$")
    public void I_call_create_subscription_endpoint() throws Throwable {
        log.info("I_call_create_subscription_endpoint");
    }

    @Then("^returns status OK$")
    public void returns_status_OK() throws Throwable {
        log.info("returns_status_OK");
    }
}
