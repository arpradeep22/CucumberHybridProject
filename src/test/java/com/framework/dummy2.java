package com.framework;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class dummy2 {
    public static void main(String[] args) throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=en-US");
        WebDriver driver = new ChromeDriver();
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        Thread.sleep(2000);

        WebElement username = driver.findElement(By.name("username"));
        username.sendKeys("Admin");
        Thread.sleep(2000);
        WebElement password = driver.findElement(By.name("password"));
        password.sendKeys("admin123");
        Thread.sleep(2000);
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();
        Thread.sleep(2000);
        driver.navigate().to("https://opensource-demo.orangehrmlive.com/web/index.php/pim/addEmployee");
       WebElement id = driver.findElement(By.cssSelector(".oxd-input.oxd-input--focus"));
        id.clear();
        id.sendKeys("12345");
        Thread.sleep(4000);
    }
}
