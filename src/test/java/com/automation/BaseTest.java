package com.automation;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
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
        ChromeOptions options = new ChromeOptions();

        // Prefer Brave when it is installed; otherwise ChromeDriver uses Chrome.
        String os = System.getProperty("os.name").toLowerCase();
        String bravePath;
        if (os.contains("win")) {
            bravePath = BRAVE_WIN;
        } else if (os.contains("mac")) {
            bravePath = BRAVE_MAC;
        } else {
            bravePath = BRAVE_LINUX;
        }

        if (new File(bravePath).isFile()) {
            options.setBinary(bravePath);
            System.out.println("Using Brave browser: " + bravePath);
        } else {
            System.out.println("Brave was not found; using the installed Chrome browser");
        }

        // Download the ChromeDriver version that matches the selected browser.
        WebDriverManager.chromedriver().setup();

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
