package com.automation;

import com.automation.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CorrectSorting01Test extends BaseTest {

    @Test(description = "TC 200 - Sort products from lowest to highest price")
    public void sortProductsByLowestPrice() {
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.open();

        productsPage.sortByLowestPrice();

        List<Double> actualPrices = productsPage.getProductPrices();
        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        Collections.sort(expectedPrices);
        Assert.assertEquals(actualPrices, expectedPrices, "Products are not in ascending price order");
    }
}
