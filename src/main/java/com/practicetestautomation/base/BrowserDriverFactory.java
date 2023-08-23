package com.practicetestautomation.base;

import java.util.logging.Level;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class BrowserDriverFactory {

	private final String browser;
	private final Logger log;

	public BrowserDriverFactory(String browser, Logger log) {
		this.browser = browser.toLowerCase();
		this.log = log;
	}


	public WebDriver createDriver() {
  		RemoteWebDriver driver;
		log.info("Create driver: " + browser);

		switch (browser) {
			case "chrome" -> driver = new ChromeDriver();
			case "firefox" -> driver = new FirefoxDriver();
			case "edge" -> driver = new EdgeDriver();
			default -> throw new IllegalArgumentException("Invalid browser value: " + browser);
		}

		Capabilities caps = driver.getCapabilities();
		String browserName = caps.getBrowserName();
		String browserVersion = caps.getBrowserVersion();
		log.info("Browser: " + browserName);
		log.info("Version: " + browserVersion);

		return driver;
    }
}
