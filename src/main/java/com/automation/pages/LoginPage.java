package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // The site uses dropdowns for username and password, not regular text fields
    private final By signInBtn      = By.id("signin");
    private final By usernameInput  = By.id("username");
    private final By passwordInput  = By.id("password");
    private final By loginBtn       = By.id("login-btn");
    private final By errorMsg       = By.cssSelector(".api-error");

    // The selected options inside the dropdowns
    // username: demouser       → #react-select-2-option-0-0
    // password: testingisfun99 → #react-select-3-option-0-0
    private final By usernameOption = By.id("react-select-2-option-0-0");
    private final By passwordOption = By.id("react-select-3-option-0-0");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void open() {
        // Navigate to the site and wait until the Sign In button is clickable
        driver.get("https://bstackdemo.com/");
        wait.until(ExpectedConditions.elementToBeClickable(signInBtn));
        System.out.println("Opened site: " + driver.getTitle());
    }

    public void login() {
        // 1. Click the Sign In button to open the login page
        driver.findElement(signInBtn).click();
        System.out.println("Clicked Sign In button");

        // 2. Open the username dropdown and select 'demouser'
        wait.until(ExpectedConditions.elementToBeClickable(usernameInput)).click();
        wait.until(ExpectedConditions.elementToBeClickable(usernameOption)).click();
        System.out.println("Selected username: demouser");

        // 3. Open the password dropdown and select the password
        wait.until(ExpectedConditions.elementToBeClickable(passwordInput)).click();
        wait.until(ExpectedConditions.elementToBeClickable(passwordOption)).click();
        System.out.println("Selected password");

        // 4. Click the Log In button to submit the form
        wait.until(ExpectedConditions.elementToBeClickable(loginBtn)).click();
        System.out.println("Clicked Log In button");
    }

    // Returns true if an error message is displayed on the page
    public boolean isErrorDisplayed() {
        return !driver.findElements(errorMsg).isEmpty();
    }

    // Returns the text of the error message
    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMsg)).getText();
    }
}