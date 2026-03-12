package com.framework.pages;

import com.framework.utils.ElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private WebDriver driver;
    private ElementUtils elementUtils;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        elementUtils = new ElementUtils(driver);
    }

    private By usernameField = By.name("username");
    private By passwordField = By.name("password");
    private By loginButton = By.xpath("//button[@type='submit']");
    private By forgetpwdButton = By.linkText("Forgot your password? ");
    private By message = By.xpath("//p[@class='oxd-text oxd-text--p oxd-alert-content-text']");
    private By fieldMsg = By.xpath("//span[@class='oxd-text oxd-text--span oxd-input-field-error-message oxd-input-group__message']");
    private By pwdMsg = By.xpath("//span[@class='oxd-text oxd-text--span oxd-input-field-error-message oxd-input-group__message']");
    private By userDropdown = By.xpath("//p[@class='oxd-userdropdown-name']");
    private By userLogout = By.xpath("//a[normalize-space()='Logout']");

    public void enterUsername(String text) {
        elementUtils.doSendKeys(usernameField, text);
    }

    public void enterPassword(String text) {
        elementUtils.doSendKeys(passwordField, text);
    }

    public void clickLogin() {
        elementUtils.doClick(loginButton);
    }

    public String getPagetitle() {
        return elementUtils.PageTitle();
    }

    public String getPageURL() {
        return elementUtils.pageURL();
    }

    public void dummymethod(String text) {
        System.out.println(text);
    }

    public String getMessage() {
        try {
            return elementUtils.LoginPageMsg(message);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFieldMessage() {
        try {
            return elementUtils.LoginPageMsg(fieldMsg);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void clickUserDropdown(){
        elementUtils.doClick(userDropdown);
    }
    public void clickUserLogout(){
        elementUtils.doClick(userLogout);
    }
}



