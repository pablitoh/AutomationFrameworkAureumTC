import jdk.jfr.Description;
import models.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.CartPage;
import pages.ProductListPage;
import setup.ValidUserTest;
import utils.TestDataManager;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UI")
@Tag("Cart")
@DisplayName("Cart Tests")
public class CartTests extends ValidUserTest {

    @Test
    @Description("Verify that specific products are present in the cart")
    @DisplayName("TC-0014 | Verify that specific products are present in the cart")
    public void verifySpecificProductsInCart() {
        ProductListPage productListPage = new ProductListPage();
        List<String> productNames = TestDataManager.getAllProductNames();
        // Retrieve full product details before adding to the cart
        List<Product> expectedProducts = productNames.stream()
                .map(productListPage::getProductDetails) // Get price and description
                .collect(Collectors.toList());
        // Add products to cart
        productListPage.addProductsToCart(productNames);
        CartPage cartPage = productListPage.getHeader().goToCart();
        assertThat(cartPage.areProductsInCart(expectedProducts))
                .as("Verify that the correct products with accurate prices and descriptions are in the cart")
                .isTrue();
    }


}
