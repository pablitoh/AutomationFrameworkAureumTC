import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import jdk.jfr.Description;
import models.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.CheckoutCompletePage;
import pages.CheckoutOverviewPage;
import pages.ProductListPage;
import setup.ValidUserTest;
import utils.TestDataManager;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UI")
@Tag("Checkout")
@Epic("Checkout")
@DisplayName("Checkout Tests")
public class CheckoutTests extends ValidUserTest {

    @Test
    @Tag("Integration")
    @Description("Verify checkout overview with three products")
    @DisplayName("TC-0008 | Verify checkout overview with three products")
    public void verifyCheckoutOverview() {
        Allure.step("Add products to cart");
        ProductListPage productListPage = new ProductListPage();
        List<String> productNames = TestDataManager.getThreeRandomProductNames();

        // Get product details before adding to cart
        List<Product> expectedProducts = productNames.stream()
                .map(productListPage::getProductDetails)
                .collect(Collectors.toList());

        //Actions
        CheckoutOverviewPage checkoutOverview =
                productListPage.addProductsToCart(productNames)
                        .getHeader()
                        .goToCart()
                        .proceedToCheckout()
                        .fillCheckoutForm(getUser())
                        .submitCheckout();

        Allure.step("Verify checkout overview");

        assertThat(checkoutOverview.areProductsCorrect(expectedProducts))
                .as("Verify that the correct products and prices are in checkout overview")
                .isTrue();

        assertThat(checkoutOverview.isTotalCorrect())
                .as("Verify total price is correct including tax")
                .isTrue();


        CheckoutCompletePage checkoutCompletePage = checkoutOverview.finishCheckout();

        assertThat(checkoutCompletePage.isPageDisplayed())
                .as("Verify checkout complete page is displayed")
                .isTrue();

        assertThat(checkoutCompletePage.isCompleteHeaderCorrect())
                .as("Verify thank you header is correct")
                .isTrue();

        assertThat(checkoutCompletePage.isCompleteTextCorrect())
                .as("Verify order confirmation text is correct")
                .isTrue();
    }
}
