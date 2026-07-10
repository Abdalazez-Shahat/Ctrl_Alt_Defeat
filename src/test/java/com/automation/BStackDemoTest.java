package com.automation;

import com.automation.pages.LoginPage;
import com.automation.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BStackDemoTest extends BaseTest {

    // Test 1 - Login and add the first product to the cart
    @Test(description = "Login and add the first product to the cart")
    public void testLoginAndAddFirstProduct() {
        LoginPage loginPage       = new LoginPage(driver);
        ProductsPage productsPage = new ProductsPage(driver);

        // 1. Open the website
        loginPage.open();

        // 2. Perform login
        loginPage.login();

        // 3. Verify that the products page is loaded
        Assert.assertTrue(productsPage.isLoaded(), "Products page did not load!");

        // 4. Add the first product to the cart
        String addedProduct = productsPage.addFirstProductToCart();

        // 5. Verify that the cart was updated
        int count = productsPage.getCartCount();
        Assert.assertEquals(count, 1, "Cart count is not 1!");
        System.out.println("Test 1 passed! Product [ " + addedProduct + " ] was added to the cart");
    }

    // Test 2 - Login and add a specific product by name to the cart
    @Test(description = "Login and add iPhone to the cart")
    public void testLoginAndAddSpecificProduct() {
        LoginPage loginPage       = new LoginPage(driver);
        ProductsPage productsPage = new ProductsPage(driver);

        // Open the website and login
        loginPage.open();
        loginPage.login();

        // Verify that the products page is loaded
        Assert.assertTrue(productsPage.isLoaded());

        // Add iPhone to the cart by searching for it by name
        productsPage.addProductToCartByName("iPhone");

        // Verify that the cart was updated
        int count = productsPage.getCartCount();
        Assert.assertEquals(count, 1, "Cart count is not 1!");
        System.out.println("Test 2 passed! iPhone was added to the cart");
    }

    // Test 3 - Login, add multiple products, and open the cart
    @Test(description = "Login, add two products, and open the cart")
    public void testLoginAddMultipleAndOpenCart() {
        LoginPage loginPage       = new LoginPage(driver);
        ProductsPage productsPage = new ProductsPage(driver);

        // Open the website and login
        loginPage.open();
        loginPage.login();

        // Verify that the products page is loaded
        Assert.assertTrue(productsPage.isLoaded());

        // Add the first product and a Samsung Galaxy product to the cart
        productsPage.addFirstProductToCart();
        productsPage.addProductToCartByName("Galaxy");

        // Verify that the cart has at least one item
        int count = productsPage.getCartCount();
        System.out.println("Number of items in cart: " + count);
        Assert.assertTrue(count >= 1, "Cart is empty!");

        // Open the cart and verify it is visible
        productsPage.openCart();
        Assert.assertTrue(productsPage.isCartVisible(), "Cart did not open!");
        System.out.println("Test 3 passed! Cart opened successfully");
    }
}
