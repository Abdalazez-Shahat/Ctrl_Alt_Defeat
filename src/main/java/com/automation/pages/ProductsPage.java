package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ProductsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators for product cards and their elements
    private final By productCards = By.cssSelector(".shelf-container .shelf-item");
    private final By productName  = By.cssSelector(".shelf-item__title");
    private final By productPrice = By.cssSelector(".shelf-item__price .val");
    private final By addToCartBtn = By.cssSelector(".shelf-item__buy-btn");
    private final By productCount = By.cssSelector(".products-found h3");
    private final By sortSelect   = By.cssSelector(".sort select");

    // Locators for the cart
    private final By cartIcon    = By.cssSelector(".bag--float-cart-closed");
    private final By cartCount   = By.cssSelector(".bag__quantity");
    private final By cartSidebar = By.cssSelector(".float-cart--open");
    private final By cartItems   = By.cssSelector(".shelf-item");

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void open() {
        driver.get("https://bstackdemo.com/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(productCards));
        waitForProductsToMatchHeader();
        System.out.println("Opened products page with " + getDisplayedProductCount() + " products");
    }

    // Waits until products are visible and checks if the page URL is correct
    public boolean isLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productCards));
        boolean loaded = driver.getCurrentUrl().contains("bstackdemo.com");
        System.out.println("Products page loaded: " + driver.getCurrentUrl());
        return loaded;
    }

    public void selectVendor(String vendor) {
        setVendorSelected(vendor, true);
    }

    public void deselectVendor(String vendor) {
        setVendorSelected(vendor, false);
    }

    public boolean isVendorSelected(String vendor) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(vendorCheckbox(vendor))).isSelected();
    }

    public int getDisplayedProductCount() {
        return wait.until(driver -> {
            try {
                return readDisplayedProductCount();
            } catch (RuntimeException e) {
                return null;
            }
        });
    }

    public int getProductCardCount() {
        waitForProductsToMatchHeader();
        return driver.findElements(productCards).size();
    }

    public List<String> getProductTitles() {
        waitForProductsToMatchHeader();
        List<String> titles = new ArrayList<>();
        for (WebElement product : driver.findElements(productCards)) {
            titles.add(product.findElement(productName).getText().trim());
        }
        return titles;
    }

    public List<Double> getProductPrices() {
        waitForProductsToMatchHeader();
        List<Double> prices = new ArrayList<>();
        for (WebElement product : driver.findElements(productCards)) {
            String priceText = product.findElement(productPrice).getText();
            prices.add(Double.parseDouble(priceText.replaceAll("[^0-9.]", "")));
        }
        return prices;
    }

    public void sortByLowestPrice() {
        sortProducts("Lowest to highest", true);
    }

    public void sortByHighestPrice() {
        sortProducts("Highest to lowest", false);
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
        if (!isCartVisible()) {
            wait.until(ExpectedConditions.elementToBeClickable(cartIcon)).click();
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartSidebar));
        System.out.println("Cart opened successfully");
    }

    // Returns true if the cart sidebar is visible on the page
    public boolean isCartVisible() {
        return driver.findElements(cartSidebar).stream().anyMatch(WebElement::isDisplayed);
    }

    private void setVendorSelected(String vendor, boolean selected) {
        By checkbox = vendorCheckbox(vendor);
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(checkbox));
        if (input.isSelected() == selected) {
            return;
        }

        int previousCount = getDisplayedProductCount();
        wait.until(ExpectedConditions.elementToBeClickable(vendorLabel(vendor))).click();
        wait.until(driver -> driver.findElement(checkbox).isSelected() == selected);
        wait.until(driver -> {
            try {
                return readDisplayedProductCount() != previousCount;
            } catch (RuntimeException e) {
                return false;
            }
        });
        waitForProductsToMatchHeader();
        System.out.println((selected ? "Selected" : "Deselected") + " vendor: " + vendor);
    }

    private void sortProducts(String visibleText, boolean ascending) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(sortSelect));
        new Select(dropdown).selectByVisibleText(visibleText);
        wait.until(driver -> {
            try {
                List<Double> prices = readProductPricesIfStable();
                return prices != null && pricesAreOrdered(prices, ascending);
            } catch (RuntimeException e) {
                return false;
            }
        });
        System.out.println("Sorted products: " + visibleText);
    }

    private void waitForProductsToMatchHeader() {
        wait.until(driver -> {
            try {
                return driver.findElements(productCards).size() == readDisplayedProductCount();
            } catch (RuntimeException e) {
                return false;
            }
        });
    }

    private int readDisplayedProductCount() {
        String text = driver.findElement(productCount).getText();
        return Integer.parseInt(text.replaceAll("[^0-9]", ""));
    }

    private List<Double> readProductPricesIfStable() {
        List<WebElement> products = driver.findElements(productCards);
        if (products.size() != readDisplayedProductCount()) {
            return null;
        }

        List<Double> prices = new ArrayList<>();
        for (WebElement product : products) {
            String priceText = product.findElement(productPrice).getText();
            prices.add(Double.parseDouble(priceText.replaceAll("[^0-9.]", "")));
        }
        return prices;
    }

    private boolean pricesAreOrdered(List<Double> prices, boolean ascending) {
        for (int i = 1; i < prices.size(); i++) {
            int comparison = Double.compare(prices.get(i - 1), prices.get(i));
            if ((ascending && comparison > 0) || (!ascending && comparison < 0)) {
                return false;
            }
        }
        return true;
    }

    private By vendorCheckbox(String vendor) {
        return By.cssSelector(".filters input[type='checkbox'][value='" + vendor + "']");
    }

    private By vendorLabel(String vendor) {
        return By.xpath("//div[contains(@class,'filters')]//input[@value='" + vendor
                + "']/following-sibling::span[contains(@class,'checkmark')]");
    }
}
