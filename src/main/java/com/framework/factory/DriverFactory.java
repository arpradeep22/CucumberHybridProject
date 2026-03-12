package com.framework.factory;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

public class DriverFactory {
    WebDriver driver;
    public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    public WebDriver init_driver(String browser) {

        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();

            //  Headless mode — comment this line out to run headed for debugging
            options.addArguments("--headless=new");

            //  Essential flags for headless stability (especially on Windows/CI)
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");

            //  Set a real full-screen resolution — Vue.js apps need full viewport
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--start-maximized");

            //  Prevents "Chrome is being controlled by automated software" bar stealing space
            options.addArguments("--disable-infobars");
            options.addArguments("--disable-extensions");

            //  Fixes font rendering and layout issues in headless
            options.addArguments("--force-device-scale-factor=1");

            //  Prevent headless detection by the site
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

            tlDriver.set(new ChromeDriver(options));

        } else if (browser.equalsIgnoreCase("chrome-headed")) {
            // ✅ Convenience: use "chrome-headed" in config.properties to run headed for debugging
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("--disable-infobars");
            tlDriver.set(new ChromeDriver(options));

        } else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            // ✅ Firefox headless support too
            FirefoxOptions ffOptions = new FirefoxOptions();
            ffOptions.addArguments("--headless");
            ffOptions.addArguments("--width=1920");
            ffOptions.addArguments("--height=1080");
            tlDriver.set(new FirefoxDriver(ffOptions));

        } else if (browser.equalsIgnoreCase("safari")) {
            tlDriver.set(new SafariDriver());

        } else {
            throw new RuntimeException("❌ Invalid browser value in config.properties: '" + browser
                    + "'. Use: chrome, chrome-headed, firefox, safari");
        }

        getDriver().manage().deleteAllCookies();

        // ✅ Only maximize for non-headless — headless ignores maximize
        // Window size is already set via --window-size arg above
        return getDriver();
    }

    public static synchronized WebDriver getDriver() {
        return tlDriver.get();
    }
}
