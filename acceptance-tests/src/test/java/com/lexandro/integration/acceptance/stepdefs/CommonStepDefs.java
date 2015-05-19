package com.lexandro.integration.acceptance.stepdefs;

import cucumber.api.java.en.Then;

import static com.lexandro.integration.acceptance.data.CucumberData.resultActions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommonStepDefs extends AbstractStepDefs {

    @Then("^returns ok status$")
    public void returns_ok_status() throws Throwable {
        resultActions.andExpect(status().isOk());
    }
}
