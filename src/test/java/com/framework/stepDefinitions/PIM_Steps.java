package com.framework.stepDefinitions;

import com.framework.factory.DriverFactory;
import com.framework.pages.PIM_Page;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

public class PIM_Steps {

    // ✅ Injected by Cucumber PicoContainer — shares the SAME Login_Steps instance
    // that Cucumber already manages, so loginPage is never null
    private final Login_Steps loginSteps;
    private WebDriver driver;
    private PIM_Page pimPage;

    public PIM_Steps(Login_Steps loginSteps) {
        this.loginSteps = loginSteps;
    }

    @Step("the user is logged into the HR system")
    @Given("the user is logged into the HR system")
    public void loginUser() {
        // ✅ Reuses the shared Login_Steps instance — same driver, same loginPage object
        loginSteps.the_user_is_logged_in_with_valid_credentials();
        loginSteps.the_user_should_be_redirected_to_the_dashboard();
        loginSteps.the_user_should_see_the_welcome_message();
    }

    @Step("the user navigates to the {string} page")
    @Given("the user navigates to the {string} page")
    public void navigateToPage(String pageName) {
        // ✅ Gets the same ThreadLocal driver — no new browser opened
        driver = DriverFactory.getDriver();
        pimPage = new PIM_Page(driver);
        pimPage.clickPimButton();
        String navigated_url = pimPage.getUrl();
        Assert.assertTrue(navigated_url.contains(pageName));
        pimPage.clickempListbtn();
    }

    /* ------------------------------------------------------------------ */
    /*  Employee actions                                                    */
    /* ------------------------------------------------------------------ */
    @Step("the user clicks the {string} button")
    @When("the user clicks the {string} button")
    public void the_user_clicks_the_button(String string) {
        pimPage.clickAddButton();
    }

    @Step("the user clicks the Save button")
    @When("the user clicks the Save button")
    public void the_user_clicks_the_save_button() {
        pimPage.save();
    }

    @Step("the user clicks the Search button")
    @When("the user clicks the Search button")
    public void the_user_clicks_the_search_button() {
        pimPage.search();
    }
    @Step("the user enters first name {firstName}")
    @When("the user enters first name {string}")
    public void enterFirstName(String firstName) {
        pimPage.enterFirstname(firstName);
    }
    @Step("the user enters middle name {string}")
    @When("the user enters middle name {string}")
    public void enterMiddleName(String middleName) {
        pimPage.enterMiddlename(middleName);
    }
    @Step("the user enters last name {string}")
    @When("the user enters last name {string}")
    public void enterLastName(String lastName) {
        pimPage.enterLastname(lastName);
    }
    @Step("the user enters email {string}")
    @When("the user enters email {string}")
    public void enterEmail(String email) {
        // TODO: Enter email
    }
    @Step("the user enters employee ID {string} into the search field")
    @When("the user enters employee ID {string} into the search field")
    public void enterEmployeeIdForSearch(String employeeId) {
        pimPage.enterEmpID(employeeId);
    }
    @Step("the user updates the email to {string}")
    @When("the user updates the email to {string}")
    public void updateEmail(String newEmail) {
        // TODO: Update email
    }
    @Step("the user confirms the deletion")
    @When("the user confirms the deletion")
    public void confirmDeletion() {
        // TODO: Confirm deletion
    }

    /* ------------------------------------------------------------------ */
    /*  Assertions                                                          */
    /* ------------------------------------------------------------------ */
    @Step("the employee should be created successfully")
    @Then("the employee should be created successfully")
    public void verifyCreationSuccess() {
        Boolean successMsg = pimPage.validateSucessmessage();
        Assert.assertNotNull("Success message was null", successMsg);
        Assert.assertTrue(successMsg);
    }
    @Step("the employee ID should be displayed as {string}")
    @Then("the employee ID should be displayed as {string}")
    public void verifyEmployeeId(String expectedId) {
        // TODO: Verify displayed ID
    }
    @Step("the user navigates to the employee details page for {string}")
    @When("the user navigates to the employee details page for {string}")
    public void the_user_navigates_to_the_employee_details_page_for(String string) {

    }
    @Step("the employee ID {string} is unique")
    @Then("the employee ID {string} is unique")
    public void verifyUniqueEmployeeId(String employeeId) {
        // TODO: Check uniqueness
    }
    @Step("the employee details page for {string} should be displayed")
    @Then("the employee details page for {string} should be displayed")
    public void verifyDetailsPage(String employeeId) {
        pimPage.tablePresentorNot();
        String id = pimPage.getId();
       // System.out.println(id);
        Assert.assertEquals(employeeId,id);
    }
    @Step("the employee name should be {string}")
    @Then("the employee name should be {string}")
    public void verifyEmployeeName(String fullName) {
        String actualname = pimPage.getFirstlastName();
       // System.out.println(fullName);
        //System.out.println(actualname);
        Assert.assertEquals(fullName,actualname);
    }
    @Step("the employee details page should show the updated email {string}")
    @Then("the employee details page should show the updated email {string}")
    public void verifyUpdatedEmail(String updatedEmail) {
        // TODO: Verify updated email
    }
    @Step("the employee {string} should no longer exist in the system")
    @Then("the employee {string} should no longer exist in the system")
    public void verifyEmployeeDeletion(String employeeId) {
        // TODO: Verify deletion
    }
    @Step("a confirmation message {string} should be displayed")
    @Then("a confirmation message {string} should be displayed")
    public void verifyConfirmationMessage(String message) {
        // TODO: Verify confirmation
    }
}
