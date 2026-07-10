package com.automation;

import com.automation.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CorrectFilteringAndSorting01Test extends BaseTest {

    @Test(description = "TC 300 - Combine vendor filtering and ascending price sorting")
    public void filterAppleAndSortByLowestPrice() {
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.open();

        productsPage.sortByLowestPrice();
        productsPage.selectVendor("Apple");

        List<String> titles = productsPage.getProductTitles();
        List<Double> actualPrices = productsPage.getProductPrices();
        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        Collections.sort(expectedPrices);

        Assert.assertEquals(productsPage.getDisplayedProductCount(), 9, "Incorrect Apple product count");
        Assert.assertTrue(titles.stream().allMatch(title -> title.startsWith("iPhone")),
                "A non-Apple product was displayed: " + titles);
        Assert.assertEquals(actualPrices, expectedPrices, "Filtered products are not sorted by ascending price");
    }
}
