package com.framework.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ElementUtils {
    private WebDriver driver;
    private WebDriverWait wait;

    public ElementUtils(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void doSendKeys(By locator, String text) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).sendKeys(text);
    }

    /**
     * Standard click with JS fallback.
     * In headless mode, elements can be "clickable" per Selenium but still fail
     * due to rendering/viewport issues — JS click bypasses that entirely.
     */
    public void doClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        try {
            element.click();
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            // ✅ JS fallback — works reliably in headless for Vue.js rendered elements
            System.out.println("⚠️ Standard click failed for [" + locator + "], using JS click. Reason: " + e.getMessage());
            jsClick(locator);
        }
    }

    /**
     * Direct JavaScript click — use for elements that are in the DOM
     * but may be hidden/overlapping in headless mode.
     */
    public void jsClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /**
     * Scrolls the element into view before interacting.
     * Headless Chrome doesn't auto-scroll, so this is essential.
     */
    public void scrollAndClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    public String PageTitle() {
        return driver.getTitle();
    }

    public String pageURL() {
        return driver.getCurrentUrl();
    }

    public String LoginPageMsg(By locator) throws InterruptedException {
        Thread.sleep(1000);
        WebElement errorMsgElement = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return errorMsgElement.getText();
    }

    public String CaptureText(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return element.getText();
    }

    public void ClearText(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).clear();
    }

    public Boolean PresenceofElement(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return element.isDisplayed();
    }
}
