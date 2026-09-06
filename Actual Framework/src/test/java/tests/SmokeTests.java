package tests;

import Pages.*;
import utils.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * SMOKE TEST SUITE
 * Covers SM-01 through SM-05 from our test case plan.
 *
 * NOTICE: Not a single By.id(...) or driver.findElement(...) appears
 * anywhere in this class. All of that lives inside the Page Object
 * classes. This class only ORCHESTRATES actions and ASSERTS results --
 * exactly the separation of concerns POM is meant to give you.
 */
public class SmokeTests extends BaseTest {

    // Test data -- kept as constants for now. In Step 6, credentials
    // and test data will move into config/properties files.
    private static final String VALID_USERNAME = "standard_user";
    private static final String VALID_PASSWORD = "secret_sauce";
    private static final String PRODUCT_ID = "sauce-labs-backpack";
    private static final String PRODUCT_NAME = "Sauce Labs Backpack";

    // ---------------------------------------------------------
    // SM-01: Login with valid credentials succeeds
    // ---------------------------------------------------------
    @Test(priority = 1)
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(VALID_USERNAME, VALID_PASSWORD);

        // Verify we landed on the inventory page by checking the URL
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("inventory.html"),
                "Expected to land on inventory page after valid login");
    }

    // ---------------------------------------------------------
    // SM-02: Add one product to cart, badge count updates to "1"
    // ---------------------------------------------------------
    @Test(priority = 2)
    public void testAddProductToCart() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(VALID_USERNAME, VALID_PASSWORD);

        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.addProductToCart(PRODUCT_ID);

        String cartCount = inventoryPage.getCartItemCount();
        Assert.assertEquals(cartCount, "1",
                "Cart badge should show 1 item after adding a product");
    }

    // ---------------------------------------------------------
    // SM-03: Navigate to cart, verify added product is listed
    // ---------------------------------------------------------
    @Test(priority = 3)
    public void testProductAppearsInCart() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(VALID_USERNAME, VALID_PASSWORD);

        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.addProductToCart(PRODUCT_ID);
        inventoryPage.goToCart();

        CartPage cartPage = new CartPage(driver);
        Assert.assertTrue(cartPage.isProductInCart(PRODUCT_NAME),
                "Added product should be visible in the cart");
    }

    // ---------------------------------------------------------
    // SM-04: Complete full checkout flow end-to-end
    // ---------------------------------------------------------
    @Test(priority = 4)
    public void testCompleteCheckoutFlow() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(VALID_USERNAME, VALID_PASSWORD);

        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.addProductToCart(PRODUCT_ID);
        inventoryPage.goToCart();

        CartPage cartPage = new CartPage(driver);
        cartPage.clickCheckout();

        CheckoutStepOnePage stepOne = new CheckoutStepOnePage(driver);
        stepOne.fillCheckoutInfo("Kiran", "Kumar", "560001");
        stepOne.clickContinue();

        CheckoutStepTwoPage stepTwo = new CheckoutStepTwoPage(driver);
        stepTwo.clickFinish();

        CheckoutCompletePage completePage = new CheckoutCompletePage(driver);
        String confirmation = completePage.getConfirmationMessage();

        Assert.assertEquals(confirmation, "Thank you for your order!",
                "Order confirmation message should appear after checkout");
    }

    // ---------------------------------------------------------
    // SM-05: Logout redirects back to login page
    // ---------------------------------------------------------
    @Test(priority = 5)
    public void testLogout() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(VALID_USERNAME, VALID_PASSWORD);

        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.logout();

        // After logout, SauceDemo redirects to the base URL (login page)
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, baseUrl,
                "Should be redirected to login page after logout");
    }
}

