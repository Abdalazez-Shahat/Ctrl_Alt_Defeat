package com.automation;

import com.automation.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class CorrectFilteringAndSorting02Test extends BaseTest {

    @Test(description = "TC 301 - Clear a selected vendor filter")
    public void clearAppleFilterAndRestoreDefaultProducts() {
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.open();
        List<String> defaultProductOrder = productsPage.getProductTitles();

        productsPage.sortByLowestPrice();
        productsPage.selectVendor("Apple");
        productsPage.deselectVendor("Apple");

        Assert.assertFalse(productsPage.isVendorSelected("Apple"), "Apple checkmark is still selected");
        Assert.assertEquals(productsPage.getDisplayedProductCount(), 25, "Not all products were restored");
        Assert.assertEquals(productsPage.getProductCardCount(), 25, "Header and product-card counts differ");
        Assert.assertEquals(productsPage.getProductTitles(), defaultProductOrder,
                "Products did not return to their default order");
    }
}
