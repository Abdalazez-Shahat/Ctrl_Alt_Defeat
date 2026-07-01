package com.automation;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;

    // Brave browser paths for each operating system
    // To find your path: open Brave and type brave://version in the address bar
    private static final String BRAVE_WIN   = "C:\\Program Files\\BraveSoftware\\Brave-Browser\\Application\\brave.exe";
    private static final String BRAVE_MAC   = "/Applications/Brave Browser.app/Contents/MacOS/Brave Browser";
    private static final String BRAVE_LINUX = "/usr/bin/brave-browser";

    @BeforeMethod
    public void setUp() {
        // Download ChromeDriver version 149 to match the current Brave version
        WebDriverManager.chromedriver().browserVersion("149").setup();

        ChromeOptions options = new ChromeOptions();

        // Detect the current OS and set the correct Brave binary path
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            options.setBinary(BRAVE_WIN);
        } else if (os.contains("mac")) {
            options.setBinary(BRAVE_MAC);
        } else {
            options.setBinary(BRAVE_LINUX);
        }

        // Start the browser maximized and disable pop-up notifications
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");

        // Launch Brave using ChromeDriver
        driver = new ChromeDriver(options);

        // Wait up to 10 seconds for elements to appear before throwing an error
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() {
        // Close the browser after each test
        if (driver != null) {
            driver.quit();
        }
    }
}