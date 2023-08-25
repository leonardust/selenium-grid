package com.practicetestautomation.base;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.testng.SauceOnDemandAuthenticationProvider;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SauceLabsFactory implements SauceOnDemandAuthenticationProvider {

    private final String browser;
    private final String platform;
    private final Logger log;
    static final SauceOnDemandAuthentication AUTHENTICATION = new SauceOnDemandAuthentication(
            System.getProperty("sauce.username"), System.getProperty("sauce.accesskey"));
    private final String sauceTestName;

    private final ThreadLocal<String> sessionId = new ThreadLocal<>();

    public SauceLabsFactory(String browser, String platform, Logger log, String sauceTestName) {
        this.browser = browser.toLowerCase();
        this.platform = platform;
        this.log = log;
        this.sauceTestName = sauceTestName;
    }


    public WebDriver createDriver() {
        RemoteWebDriver driver;
        DesiredCapabilities capabilities = new DesiredCapabilities();
        URL url;
        Map<String, Object> sauceOptions = new HashMap<>();

        log.info("Create SauceLabs instance for " + browser + " on " + platform);
        capabilities.setCapability("browserName", browser);
        capabilities.setCapability("browserVersion", "latest");
        capabilities.setCapability("platformName", platform);

        log.info("Configure Sauce labs");
        if (platform.contains("macOS")) {
            sauceOptions.put("screenResolution", "1920x1440");
        } else {
            sauceOptions.put("screenResolution", "1920x1080");
        }
        sauceOptions.put("name", sauceTestName);
        capabilities.setCapability("sauce:options", sauceOptions);

        log.info("Create remote driver");
        try {
            url = new URL("https://" + AUTHENTICATION.getUsername() + ":" + AUTHENTICATION.getAccessKey() + "@ondemand.eu-central-1.saucelabs.com:443/wd/hub");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        driver = new RemoteWebDriver(url, capabilities);
        log.info("Get session id");
        sessionId.set((driver).getSessionId().toString());

        log.info("Driver created!!!");
        log.info("Connected with node!!!");
        log.info("Platform: " + capabilities.getPlatformName());
        log.info("Browser: " + driver.getCapabilities().getBrowserName());
        log.info("Version: " + driver.getCapabilities().getBrowserVersion());

        return driver;
    }

    @Override
    public SauceOnDemandAuthentication getAuthentication() {
        return AUTHENTICATION;
    }

    public String getSessionId() {
        return sessionId.get();
    }
}
