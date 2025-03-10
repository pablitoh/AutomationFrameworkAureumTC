package pages;

import models.Product;
import org.openqa.selenium.By;
import pages.setup.BasePage;

import java.util.List;
import java.util.stream.Collectors;

public class CheckoutOverviewPage extends BasePage {

    private final By itemNames = By.cssSelector("[data-test='inventory-item-name']");
    private final By itemPrices = By.cssSelector("[data-test='inventory-item-price']");
    private final By subtotalPrice = By.cssSelector("[data-test='subtotal-label']");
    private final By taxPrice = By.cssSelector("[data-test='tax-label']");
    private final By totalPrice = By.cssSelector("[data-test='total-label']");
    private final By finishButton = By.id("finish");
    private final By cancelButton = By.id("cancel");

    /**
     * Gets the list of product names from the checkout overview page.
     *
     * @return List of product names.
     */
    public List<String> getProductNamesInCheckout() {
        return getElements(itemNames).stream().map(e -> e.getText()).collect(Collectors.toList());
    }

    /**
     * Gets the list of product prices from the checkout overview page.
     *
     * @return List of product prices as doubles.
     */
    public List<Double> getProductPricesInCheckout() {
        return getElements(itemPrices).stream()
                .map(element -> element.getText().replace("$", ""))
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }

    /**
     * Gets the subtotal price displayed on the checkout overview page.
     *
     * @return Subtotal price as a double.
     */
    public double getSubtotalPrice() {
        return Double.parseDouble(getText(subtotalPrice).replace("Item total: $", ""));
    }

    /**
     * Gets the tax amount displayed on the checkout overview page.
     *
     * @return Tax amount as a double.
     */
    public double getTaxAmount() {
        return Double.parseDouble(getText(taxPrice).replace("Tax: $", ""));
    }

    /**
     * Gets the total price displayed on the checkout overview page.
     *
     * @return Total price as a double.
     */
    public double getTotalPrice() {
        return Double.parseDouble(getText(totalPrice).replace("Total: $", ""));
    }

    /**
     * Completes the checkout process.
     */
    public CheckoutCompletePage finishCheckout() {
        click(finishButton);
        return new CheckoutCompletePage();
    }

    /**
     * Cancels the checkout process.
     */
    public void cancelCheckout() {
        click(cancelButton);
    }

    /**
     * Verifies that all expected products are present in the checkout overview.
     *
     * @param expectedProducts List of expected products.
     * @return True if all expected products match.
     */
    public boolean areProductsCorrect(List<Product> expectedProducts) {
        List<String> actualProductNames = getProductNamesInCheckout();
        List<Double> actualProductPrices = getProductPricesInCheckout();

        return expectedProducts.stream().allMatch(product ->
                actualProductNames.contains(product.getName()) &&
                        actualProductPrices.contains(product.getPrice()));
    }

    /**
     * Verifies that the total price matches the expected subtotal + tax.
     *
     * @return True if the total matches.
     */
    public boolean isTotalCorrect() {
        return getSubtotalPrice() + getTaxAmount() == getTotalPrice();
    }
}