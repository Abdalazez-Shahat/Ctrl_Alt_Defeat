package com.automation;

import com.automation.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class CorrectFiltering02Test extends BaseTest {

    @Test(description = "TC 101 - Filter products by multiple vendors")
    public void filterProductsByAppleAndOnePlus() {
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.open();

        productsPage.selectVendor("Apple");
        productsPage.selectVendor("OnePlus");

        List<String> titles = productsPage.getProductTitles();
        Assert.assertEquals(productsPage.getDisplayedProductCount(), 15,
                "Incorrect combined Apple and OnePlus product count");
        Assert.assertEquals(productsPage.getProductCardCount(), 15,
                "Header and product-card counts differ");
        Assert.assertTrue(titles.stream().allMatch(title ->
                        title.startsWith("iPhone") || title.startsWith("One Plus")),
                "A product from an unselected vendor was displayed: " + titles);
        Assert.assertTrue(titles.stream().anyMatch(title -> title.startsWith("iPhone")),
                "No Apple products were displayed");
        Assert.assertTrue(titles.stream().anyMatch(title -> title.startsWith("One Plus")),
                "No OnePlus products were displayed");
    }
}
