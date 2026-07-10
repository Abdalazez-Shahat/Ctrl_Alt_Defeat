package com.automation;

import com.automation.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class CorrectFiltering01Test extends BaseTest {

    @Test(description = "TC 100 - Filter products by a single vendor")
    public void filterProductsByApple() {
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.open();

        productsPage.selectVendor("Apple");

        List<String> titles = productsPage.getProductTitles();
        Assert.assertTrue(productsPage.isVendorSelected("Apple"), "Apple filter is not selected");
        Assert.assertEquals(productsPage.getDisplayedProductCount(), 9, "Incorrect Apple product count");
        Assert.assertEquals(productsPage.getProductCardCount(), 9, "Header and product-card counts differ");
        Assert.assertTrue(titles.stream().allMatch(title -> title.startsWith("iPhone")),
                "A non-Apple product was displayed: " + titles);
    }
}
