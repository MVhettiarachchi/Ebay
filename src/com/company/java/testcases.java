package com.company.java;

import com.company.config.PlaywrightSetup;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RelatedProductsTest extends PlaywrightSetup
{

    @BeforeEach
    public void setUp() {
        setup();
    }

    @AfterEach
    public void tearDown() {
        teardown();
    }

    @Test
    public void verifyRelatedProductsCount() {
        page.navigate("https://www.ebay.com/");
        page.fill("input[placeholder='Search for anything']", "Leather Wallet");
        page.click("button[type='submit']");

        page.click("a:has-text('Leather Wallet')");
        page.waitForSelector("#related-products-section");

        int productCount = page.locator("#related-products-section .product-item").count();
        assertEquals(6, productCount, "The number of related products is not six.");
    }

    @Test
    public void verifyNoRelatedProductsMessage() {
        // Mock API response for no related products
        page.route("**/api/related-products", route -> route.fulfill(
                new Page.RouteFulfillOptions().setStatus(200).setBody("{\"products\": []}")
        ));

        page.navigate("https://www.ebay.com/");
        page.fill("input[placeholder='Search for anything']", "Unique Product");
        page.click("button[type='submit']");

        String message = page.textContent("#related-products-section");
        assertEquals("No related products found", message, "Expected message for no related products is missing.");
    }
}
