package com.qa.testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterClass;


@CucumberOptions(
        features = "src/test/resources/features/dashboard.feature",
        glue={"com.qa.stepDefinitions","com.qa.hooks"},
        monochrome = true,
        dryRun = false,
        tags="@regression",
        plugin = {"com.qa.hooks.CustomReportListner"},
        publish = true
)

public class Runner extends AbstractTestNGCucumberTests {
    @AfterClass(alwaysRun = true)
    @Override
    public void tearDownClass()
    {
        try
        {
            super.tearDownClass();
        }
        catch(RuntimeException e)
        {

        }
    }
}
