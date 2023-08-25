package com.practicetestautomation.base;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class GridFactory {

    private final String browser;
    private final String platform;
    private final Logger log;

    public GridFactory(String browser, String platform, Logger log) {
        this.browser = browser.toLowerCase();
        this.platform = platform;
        this.log = log;
    }


    public WebDriver createDriver() {
        RemoteWebDriver driver;
        DesiredCapabilities capabilities = new DesiredCapabilities();
        log.info("Connecting to the node with: " + browser);

        log.info("Setting platform to " + platform + "...");
        if (platform.equals("MAC")) {
            capabilities.setPlatform(Platform.MAC);
        } else {
            capabilities.setPlatform(Platform.WIN11);
        }

        capabilities.getPlatformName();

        switch (browser) {
            case "firefox" -> capabilities.setBrowserName("firefox");
            case "msedge" -> capabilities.setBrowserName("MicrosoftEdge");
            default -> capabilities.setBrowserName("chrome");
        }

        URL url;
        try {
            url = new URL("http://localhost:4444");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        driver = new RemoteWebDriver(url, capabilities);

        log.info("Connected with node!!!");
        log.info("Platform: " + capabilities.getPlatformName());
        log.info("Browser: " + driver.getCapabilities().getBrowserName());
        log.info("Version: " + driver.getCapabilities().getBrowserVersion());

        return driver;
    }
}
