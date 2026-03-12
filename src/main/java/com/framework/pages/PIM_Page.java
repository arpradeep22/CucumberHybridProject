package com.framework.pages;

import com.framework.utils.ElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PIM_Page {

    private WebDriver driver;
    private ElementUtils elementUtils;

    public PIM_Page(WebDriver driver) {
        this.driver = driver;
        this.elementUtils = new ElementUtils(driver);
    }

    private By pimButton          = By.linkText("PIM");
    private By employeeListButton = By.linkText("Employee List");
    private By addButton          = By.xpath("//button[normalize-space()='Add']");
    private By firstnameField     = By.name("firstName");
    private By middlenameField    = By.name("middleName");
    private By lastnameField      = By.name("lastName");
    private By saveButton         = By.xpath("//button[normalize-space()='Save']");
    private By searchButton       = By.xpath("//button[normalize-space()='Search']");
    private By tableLocator       = By.cssSelector("[role='table']");

    // ✅ Toast success message
    private By successMessage = By.xpath("//*[@class='oxd-text oxd-text--p oxd-text--toast-title oxd-toast-content-text']");

    // ✅ Employee ID input field on Add Employee form
    private By employeeIDField = By.xpath("(//input[@placeholder='Type for hints...'])[1]");

    // ✅ Fixed: correct absolute XPaths for table cells after search results load
    private By idCell        = By.xpath("//div[@role='rowgroup'][2]//div[@role='row'][1]//div[@role='cell'][2]");
    private By firstNameCell = By.xpath("//div[@role='rowgroup'][2]//div[@role='row'][1]//div[@role='cell'][3]");
    private By lastNameCell  = By.xpath("//div[@role='rowgroup'][2]//div[@role='row'][1]//div[@role='cell'][4]");

    public void clickPimButton() {
        elementUtils.doClick(pimButton);
    }

    public void clickempListbtn() {
        elementUtils.doClick(employeeListButton);
    }

    public void clickAddButton() {
        elementUtils.doClick(addButton);
    }

    public void enterFirstname(String firstname) {
        elementUtils.doSendKeys(firstnameField, firstname);
    }

    public void enterMiddlename(String middlename) {
        elementUtils.doSendKeys(middlenameField, middlename);
    }

    public void enterLastname(String lastname) {
        elementUtils.doSendKeys(lastnameField, lastname);
    }

    public void enterEmpID(String empid) {
        // ✅ ClearText now uses explicit wait — no stale reference
        elementUtils.ClearText(employeeIDField);
        elementUtils.doSendKeys(employeeIDField, empid);
    }

    public void save() {
        elementUtils.doClick(saveButton);
    }

    public void search() {
        elementUtils.doClick(searchButton);
    }

    public Boolean validateSucessmessage() {
        String msg = elementUtils.CaptureText(successMessage);
        return msg.contains("Success");
    }

    public String getUrl() {
        return elementUtils.pageURL();
    }

    public void tablePresentorNot() {
        elementUtils.PresenceofElement(tableLocator);
    }

    public String getId() {
        // ✅ CaptureText now retries on StaleElementReferenceException
        return elementUtils.CaptureText(idCell);
    }

    public String getFirstlastName() {
        // ✅ Fixed XPaths — were missing // prefix, now absolute paths
        String firstName = elementUtils.CaptureText(firstNameCell);
        String lastName  = elementUtils.CaptureText(lastNameCell);
        return firstName + " " + lastName;
    }
}
