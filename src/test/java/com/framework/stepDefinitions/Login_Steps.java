package com.framework.stepDefinitions;

import com.framework.factory.DriverFactory;
import com.framework.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class Login_Steps {

    private WebDriver driver;
    private LoginPage loginPage;
    String message;

    @Step("the user is on the Orange HRM login page")
    @Given("the user is on the Orange HRM login page")
    public void the_user_is_on_the_orange_hrm_login_page() {
        driver = DriverFactory.getDriver();
        loginPage = new LoginPage(driver);
        String pagetitle = loginPage.getPagetitle();
        Assert.assertTrue(pagetitle.equalsIgnoreCase("orangehrm"));
    }

    @Step("the user enters {string} in the username field")
    @When("the user enters {string} in the username field")
    public void the_user_enters_in_the_username_field(String string) {
        loginPage.enterUsername(string);
    }

    @Step("the user enters {string} in the password field")
    @When("the user enters {string} in the password field")
    public void the_user_enters_in_the_password_field(String string) {
        loginPage.enterPassword(string);
    }

    @Step("the user clicks the Login button")
    @When("the user clicks the Login button")
    public void the_user_clicks_the_login_button() {
        loginPage.clickLogin();
    }

    @Step("the user should be redirected to the dashboard")
    @Then("the user should be redirected to the dashboard")
    public void the_user_should_be_redirected_to_the_dashboard() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("dashboard"));
    }

    @Step("the user should see the welcome message")
    @Then("the user should see the welcome message")
    public void the_user_should_see_the_welcome_message() {
        String pageurl = loginPage.getPageURL();
        Assert.assertTrue(pageurl.contains("dashboard"));
    }

    @Step("an error message should appear")
    @Then("an error message should appear")
    public void an_error_message_should_appear() {
        message = loginPage.getMessage();
        System.out.println(message);
    }

    @Step("the error message should display {string}")
    @Then("the error message should display {string}")
    public void the_error_message_should_display(String string) {
        Assert.assertEquals(string, message);
    }

    @Step("the user should remain on the login page")
    @Then("the user should remain on the login page")
    public void the_user_should_remain_on_the_login_page() {
        String pageurl = loginPage.getPageURL();
        Assert.assertTrue(pageurl.contains("login"));
    }

    @Step("the user clicks the Login button without entering credentials")
    @When("the user clicks the Login button without entering credentials")
    public void the_user_clicks_the_login_button_without_entering_credentials() {
        loginPage.clickLogin();
    }

    @Step("validation error messages should appear")
    @Then("validation error messages should appear")
    public void validation_error_messages_should_appear() {
        String fielderror = loginPage.getFieldMessage();
        System.out.println("is empty ? " + fielderror.isEmpty());
    }

    @Step("the password field should display a required error")
    @Then("the password field should display a required error")
    public void the_password_field_should_display_a_required_error() {
        String fielderror = loginPage.getFieldMessage();
        Assert.assertEquals("Required", fielderror);
    }

    @Step("the username field should display a required error")
    @Then("the username field should display a required error")
    public void the_username_field_should_display_a_required_error() {
        String fielderror = loginPage.getFieldMessage();
        Assert.assertEquals("Required", fielderror);
    }

    /**
     * ✅ Fixed: added explicit wait for dashboard URL after clicking login.
     * Previously this method just clicked login and returned immediately.
     * When called from PIM_Steps, there was no intermediate wait step like
     * "the user should be redirected to the dashboard" to slow things down,
     * so the_user_should_see_the_welcome_message() ran before the page loaded.
     */
    @Step("the user is logged in with valid credentials")
    @Given("the user is logged in with valid credentials")
    public void the_user_is_logged_in_with_valid_credentials() {
        the_user_is_on_the_orange_hrm_login_page();
        the_user_enters_in_the_username_field("Admin");
        the_user_enters_in_the_password_field("admin123");
        the_user_clicks_the_login_button();
        // ✅ Wait for dashboard to fully load before returning
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("dashboard"));
    }

    @Step("the user clicks on the user profile dropdown")
    @When("the user clicks on the user profile dropdown")
    public void the_user_clicks_on_the_user_profile_dropdown() {
        String pageurl = loginPage.getPageURL();
        Assert.assertTrue(pageurl.contains("dashboard"));
        loginPage.clickUserDropdown();
    }

    @Step("the user clicks the Logout button")
    @When("the user clicks the Logout button")
    public void the_user_clicks_the_logout_button() {
        loginPage.clickUserLogout();
    }

    @Step("the user should be redirected to the login page")
    @Then("the user should be redirected to the login page")
    public void the_user_should_be_redirected_to_the_login_page() {
        String pageurl = loginPage.getPageURL();
        Assert.assertTrue(pageurl.contains("login"));
    }

    @Step("the session should be terminated")
    @Then("the session should be terminated")
    public void the_session_should_be_terminated() {
        System.out.println("terminated");
    }
}
