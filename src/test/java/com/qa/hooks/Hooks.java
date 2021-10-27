package com.qa.hooks;

import com.qa.managers.FileReaderManager;
import com.qa.managers.WebDriverManager;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Hooks {
    String browser= Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("browser");
    @Before("@login")
    public void setup() throws InterruptedException
    {
        Thread.sleep(2000);
        WebDriver driver= WebDriverManager.getInstance().getDriver(browser);
        driver.get(FileReaderManager.getInstance().getConfig().getAppurl());
        driver.findElement(By.id("email")).click();
    }

    @After("@regression")
    public void failureScreenshot(Scenario scenario) throws IOException
    {
        scenario.log("finished Scenario");
        WebDriver driver=WebDriverManager.getInstance().getDriver(browser);
        if(scenario.isFailed()) {
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshotBytes, "img/png", "Error Screenshot");
            try (
                    FileOutputStream fos =
                            new FileOutputStream("Z:\\codebase\\errorscreenshot\\screenshot-" +
                                    URLEncoder.encode(scenario.getName(), StandardCharsets.UTF_8.toString()) + ".png");
                    BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                bos.write(screenshotBytes);
                bos.flush();
            }
        }
        WebDriverManager.getInstance().closeDriver(browser);
    }

    @AfterStep
    public void addScreenshot(Scenario scenario)
    {
        if(scenario.isFailed())
        {
            WebDriver driver=WebDriverManager.getInstance().getDriver(browser);
            final byte[] screenshot=((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot,"image/png","image");
        }
    }

    @AfterTest
    @Parameters({"browser"})
    public void tearDown(String browser)
    {
        WebDriverManager.getInstance().closeDriver(browser);
    }
}
