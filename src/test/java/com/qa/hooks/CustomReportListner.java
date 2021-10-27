package com.qa.hooks;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.gherkin.model.Given;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.cucumber.java.Scenario;
import com.aventstack.extentreports.Status;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.*;
import org.testng.Reporter;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomReportListner implements EventListener {
    private ThreadLocal<ExtentSparkReporter> spark = new ThreadLocal<>();
    private ThreadLocal<ExtentReports> extent = new ThreadLocal<>();
    private ThreadLocal<Map<String, ExtentTest>> feature = new ThreadLocal<>();
    private ThreadLocal<ExtentTest> scenario = new ThreadLocal<>();
    private ThreadLocal<ExtentTest> step = new ThreadLocal<>();
    private Map<URI, String> uriToTitleMap = new ConcurrentHashMap<>();


    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, this::runStarted);
        publisher.registerHandlerFor(TestRunFinished.class, this::runFinished);
        publisher.registerHandlerFor(TestSourceRead.class, this::featureRead);
        publisher.registerHandlerFor(TestCaseStarted.class, this::scenarioStarted);
        publisher.registerHandlerFor(TestStepStarted.class, this::stepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::stepFinished);
    };

    private void runStarted(TestRunStarted event) {
        String browser = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("browser");
        ExtentSparkReporter localspark = new ExtentSparkReporter("./ExtentReportResults-" + browser + ".html");
        spark.set(localspark);
        ExtentReports localExtent = new ExtentReports();
        extent.set(localExtent);
        localspark.config().setTheme(Theme.DARK);
        localExtent.attachReporter(localspark);
    };

    private void runFinished(TestRunFinished event) {
        extent.get().flush();
    };

    private void featureRead(TestSourceRead event) {
        if (feature.get() == null) {
            feature.set(new HashMap<String, ExtentTest>());
        }
        uriToTitleMap.putIfAbsent(event.getUri(), event.getSource().substring(9, event.getSource().indexOf('\r')));
    };

    private void scenarioStarted(TestCaseStarted event) {
        String featureSource = event.getTestCase().getUri().toString();
        if (feature.get().get(featureSource) == null) {
            feature.get().putIfAbsent(featureSource, extent.get().createTest(uriToTitleMap.get(event.getTestCase().getUri())));
        }
        scenario.set(feature.get().get(featureSource).createNode(event.getTestCase().getName()));
    };

    private void stepStarted(TestStepStarted event) {
        String stepName = "";
        String keyword = "Triggered the hook:";
        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep steps = (PickleStepTestStep) event.getTestStep();
            stepName = steps.getStep().getText();
            keyword = steps.getStep().getKeyWord();
        } else {
            HookTestStep hoo = (HookTestStep) event.getTestStep();
            stepName = hoo.getHookType().name();
        }

        step.set(scenario.get().createNode(Given.class, keyword + "" + stepName));
    };

    private void stepFinished(TestStepFinished event)
    {
        if(event.getResult().getStatus().toString()=="PASSED")
        {
            step.get().log(Status.PASS,"This Passed");
        }
        else if (event.getResult().getStatus().toString()=="SKIPPED")
        {
            step.get().log(Status.SKIP,"This status was skipped");
        }
        else
        {
            step.get().log(Status.FAIL,"This failed");
        }
    };

    private void testCaseFinished(TestCaseFinished event)
    {
        System.out.println(event);
    }
}
