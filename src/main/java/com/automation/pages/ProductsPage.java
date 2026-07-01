package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ProductsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators for product cards and their elements
    private final By productCards = By.cssSelector(".shelf-item");
    private final By productName  = By.cssSelector(".shelf-item__title");
    private final By addToCartBtn = By.cssSelector(".shelf-item__buy-btn");

    // Locators for the cart
    private final By cartIcon    = By.cssSelector(".float-cart__open-btn");
    private final By cartCount   = By.cssSelector(".bag__quantity");
    private final By cartSidebar = By.cssSelector(".float-cart");
    private final By cartItems   = By.cssSelector(".shelf-item");

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Waits until products are visible and checks if the page URL is correct
    public boolean isLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productCards));
        boolean loaded = driver.getCurrentUrl().contains("bstackdemo.com");
        System.out.println("Products page loaded: " + driver.getCurrentUrl());
        return loaded;
    }

    // Adds the first product in the list to the cart and returns its name
    public String addFirstProductToCart() {
        List<WebElement> products = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(productCards)
        );
        WebElement firstProduct = products.get(0);
        String name = firstProduct.findElement(productName).getText();
        firstProduct.findElement(addToCartBtn).click();
        System.out.println("Added to cart: " + name);
        return name;
    }

    // Loops through all products and adds the one that matches the given name
    public void addProductToCartByName(String targetName) {
        List<WebElement> products = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(productCards)
        );
        for (WebElement product : products) {
            String name = product.findElement(productName).getText();
            if (name.toLowerCase().contains(targetName.toLowerCase())) {
                product.findElement(addToCartBtn).click();
                System.out.println("Added to cart: " + name);
                return;
            }
        }
        // Throws an error if no product matched the given name
        throw new RuntimeException("Product not found: " + targetName);
    }

    // Returns the number of items currently in the cart
    public int getCartCount() {
        try {
            WebElement badge = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(cartCount)
            );
            return Integer.parseInt(badge.getText().trim());
        } catch (Exception e) {
            // Returns 0 if the cart badge is not visible
            return 0;
        }
    }

    // Clicks the cart icon and waits for the cart sidebar to appear
    public void openCart() {
        driver.findElement(cartIcon).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartSidebar));
        System.out.println("Cart opened successfully");
    }

    // Returns true if the cart sidebar is visible on the page
    public boolean isCartVisible() {
        return !driver.findElements(cartSidebar).isEmpty();
    }
}