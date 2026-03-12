package com.framework.hooks;

import com.framework.config.Config_Reader;
import com.framework.factory.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.util.Properties;
import java.util.UUID;

public class Hooks {

    private Properties properties;
    private DriverFactory driverFactory;
    Config_Reader configReader;
    private WebDriver driver;

    @Before(order = 0)
    public void configsetup() {
        configReader = new Config_Reader();
        properties = Config_Reader.initProperties();
    }

    @Before(order = 1)
    public void launchBrowser() {
        String browsername = properties.getProperty("browser");
        driverFactory = new DriverFactory();
        driver = driverFactory.init_driver(browsername);
        String url = properties.getProperty("url");
        driver.get(url);
    }

    // ✅ order=1 runs FIRST — screenshot must be taken before browser quits
    @After(order = 1)
    public void takeScreenshotOnFailure(Scenario scenario) {
        if (scenario.isFailed()) {
            WebDriver currentDriver = DriverFactory.getDriver();
            if (currentDriver != null) {
                byte[] screenshot = ((TakesScreenshot) currentDriver)
                        .getScreenshotAs(OutputType.BYTES);

                // ✅ Attach to Cucumber HTML report
                scenario.attach(screenshot, "image/png", "Screenshot on Failure");

                // ✅ Correct way to attach screenshot to Allure from a @After hook
                // Allure.addAttachment() can lose context in hooks — using lifecycle step wrapper guarantees it shows up
                AllureLifecycle lifecycle = Allure.getLifecycle();
                String stepUUID = UUID.randomUUID().toString();

                lifecycle.startStep(stepUUID, new StepResult()
                        .setName("Screenshot on Failure")
                        .setStatus(Status.FAILED));

                lifecycle.addAttachment(
                        "Screenshot - " + scenario.getName(),
                        "image/png",
                        "png",
                        new ByteArrayInputStream(screenshot)
                );

                lifecycle.stopStep(stepUUID);
            }
        }
    }

    // ✅ order=0 runs LAST — quits browser only after screenshot is captured
    @After(order = 0)
    public void quitBrowser() {
        if (DriverFactory.getDriver() != null) {
            DriverFactory.getDriver().quit();
            DriverFactory.tlDriver.remove();
        }
    }
}
