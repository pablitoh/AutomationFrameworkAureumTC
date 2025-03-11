import io.qameta.allure.Allure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.ProductListPage;
import setup.ValidUserTest;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UI")
@DisplayName("Inventory Tests")
public class InventoryTests extends ValidUserTest {

    @Test
    @Tag("Sorting")
    @DisplayName("TC-0004 | Verify sorting options by Price (low to high)")
    public void verifySortingLowToHigh() {
        ProductListPage productListPage = new ProductListPage();
        productListPage.selectSortOption("Price (low to high)");
        Allure.step("Verify product prices are sorted in ascending order");
        assertThat(productListPage.getProductPrices())
                .as("Verify product prices are sorted in ascending order")
                .isSorted();
    }

    @Test
    @Tag("Sorting")
    @DisplayName("TC-0005 | Verify sorting options by Price (high to low)")
    public void verifySortingHighToLow() {
        ProductListPage productListPage = new ProductListPage();
        productListPage.selectSortOption("Price (high to low)");
        Allure.step("Verify product prices are sorted in descending order");
        assertThat(productListPage.getProductPrices())
                .as("Verify product prices are sorted in descending order")
                .isSortedAccordingTo((a, b) -> Double.compare(b, a));
    }

    @Test
    @Tag("Sorting")
    @DisplayName("TC-0006 | Verify sorting options by Name (A to Z)")
    public void verifySortingNameAToZ() {
        ProductListPage productListPage = new ProductListPage();
        productListPage.selectSortOption("Name (A to Z)");
        Allure.step("Verify product names are sorted in ascending alphabetical order");
        assertThat(productListPage.getProductNames())
                .as("Verify product names are sorted in ascending alphabetical order")
                .isSorted();
    }

    @Test
    @Tag("Sorting")
    @DisplayName("TC-0007 | Verify sorting options by Name (Z to A)")
    public void verifySortingNameZToA() {
        ProductListPage productListPage = new ProductListPage();

        productListPage.selectSortOption("Name (Z to A)");
        Allure.step("Verify product names are sorted in descending alphabetical order");
        assertThat(productListPage.getProductNames())
                .as("Verify product names are sorted in descending alphabetical order")
                .isSortedAccordingTo((a, b) -> b.compareTo(a));
    }


}




