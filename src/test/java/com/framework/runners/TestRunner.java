package com.framework.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(

        //tags = "@smoke",
        features = {"src/test/resources/features"},
        glue = {"com.framework.stepDefinitions", "com.framework.hooks"},
        monochrome = true,
        dryRun = false,
        plugin = {
                "pretty",
                //  Allure plugin — writes JSON results to allure-results/ folder
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                // ✅ Standard Cucumber HTML report
                "html:target/cucumber-reports/cucumber.html",
                // ✅ JSON report (useful for CI integrations)
                "json:target/cucumber-reports/cucumber.json"
        }
)
public class TestRunner {
}
