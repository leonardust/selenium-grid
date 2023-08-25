package com.practicetestautomation.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.Properties;

@Listeners({SauceLabsTestListener.class})
public class BaseTest {

    protected WebDriver driver;
    protected Logger log;

    @Parameters({"browser", "environment", "platform"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method, @Optional("chrome") String browser, @Optional("local") String environment, @Optional("WIN11") String platform, ITestContext ctx) {
        log = LogManager.getLogger(ctx.getCurrentXmlTest().getSuite().getName());
        setTestProperties();
        ctx.setAttribute("sauce", false);
        switch (environment) {
            case "grid" -> driver = new GridFactory(browser, platform, log).createDriver();
            case "saucelabs" -> {
                ctx.setAttribute("sauce", true);
                String sauceTestName = ctx.getName() + " | " + method.getName() + " | " + browser + " | " + platform;
                SauceLabsFactory factory = new SauceLabsFactory(browser, platform, log, sauceTestName);
                driver = factory.createDriver();
                ctx.setAttribute("sessionId", factory.getSessionId());
            }
            default -> driver = new BrowserDriverFactory(browser, log).createDriver();
        }
        Dimension windowSize = new Dimension(1920, 1080);
        driver.manage().window().setSize(windowSize);
    }

    private void setTestProperties() {
        Properties props = System.getProperties();
        try {
            props.load(new FileInputStream("src/main/resources/test.properties"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        log.info("Close driver");
        driver.quit();
    }

}
