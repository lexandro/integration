package com.lexandro.integration.acceptance;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(monochrome = true, format = {"pretty"}, strict = true, glue = {"com.lexandro.integration.acceptance"}, tags = "@imaginarium")
public class ImaginariumCucumberTest {

}
