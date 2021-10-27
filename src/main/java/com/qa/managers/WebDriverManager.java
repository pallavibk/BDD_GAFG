package com.qa.managers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class WebDriverManager {
    private Map<String, WebDriver> driverMap;
    private static WebDriverManager instance = new WebDriverManager();

    private WebDriverManager() {
        this.driverMap = new ConcurrentHashMap<>(4);

    }

    public static WebDriverManager getInstance() {
        return instance;
    }

    private void createDriver(String browser) {
        WebDriver driver = null;
        String osname = System.getProperty("os.name");
        switch (browser) {
            case "firefox": {
                System.setProperty("webdriver.gecko.driver", "z:\\dev\\geckodriver.exe");
                System.setProperty(FirefoxDriver.SystemProperty.BROWSER_BINARY, "C:\\users\\pallavi.kundagol\\AppData\\Local\\Mozilla Firefox\\firefox.exe");
                driver = new FirefoxDriver();
            }
            break;
            case "chrome":
            default: {
                if (osname.contains("Windows")) {
                    System.setProperty("webdriver.chrome.driver", "/Users/pallavik/gafg-selenium/src/main/resources/chromedriver.exe");
                    driver = new ChromeDriver();
                } else {
                    System.setProperty("webdriver.chrome.driver", "/Users/pallavik/gafg-selenium/src/main/resources/chromedriver");
//                    ChromeOptions options = new ChromeOptions();
//                    options.setBinary("/usr/bin/chromium");
//                    options.addArguments("--no-sandbox", "--headless", "--disabled-gpu", "--ignore-certificate-errors");
//                    options.addArguments("start-maximized");
//                    options.addArguments("--disable-browser-side-navigation");
//                    options.addArguments("enable-automation");
//                    options.addArguments("window-size=1280,720");
//                    options.addArguments("--whitelisted-ips");
//                    options.addArguments("--disabled-dev-shm-usage");
//                    driver = new ChromeDriver(options);
                    driver = new ChromeDriver();
                    driver.get("https://www.demoqa.com/login");
                }
            }

        }
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        // driver.manage().timeouts().implicitlyWait(TestUtil.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
        // driver.manage().timeouts().pageLoadTimeout(TestUtil.IMPLICIT_WAIT, TimeUnit.SECONDS);
        this.driverMap.put(browser, driver);
    }

    public WebDriver getDriver(String browser) {
        if (this.driverMap.get(browser) == null) {
            synchronized (this) {
                createDriver(browser);
            }
        }
        return this.driverMap.get(browser);
    }

    public void closeDriver(String browser) {
        WebDriver driver = this.driverMap.get(browser);
        if (driver != null) {
            this.driverMap.remove(browser);
            driver.close();
        }
    }
}
