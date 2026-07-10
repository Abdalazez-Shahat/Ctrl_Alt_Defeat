package com.automation;

import com.automation.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CorrectSorting02Test extends BaseTest {

    @Test(description = "TC 201 - Sort products from highest to lowest price")
    public void sortProductsByHighestPrice() {
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.open();

        productsPage.sortByHighestPrice();

        List<Double> actualPrices = productsPage.getProductPrices();
        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        expectedPrices.sort(Collections.reverseOrder());
        Assert.assertEquals(actualPrices, expectedPrices, "Products are not in descending price order");
    }
}
