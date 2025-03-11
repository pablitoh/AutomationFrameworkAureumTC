package pages;

import models.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pages.setup.BasePage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CartPage extends BasePage {

    private static final By CART_ITEMS = By.cssSelector("div[data-test='inventory-item-name']");
    private static final By CART_PRICES = By.cssSelector("div[data-test='inventory-item-price']");
    private static final By CART_DESCRIPTIONS = By.cssSelector("div[data-test='inventory-item-desc']");
    private static final By CHECKOUT_BUTTON = By.cssSelector("button[data-test='checkout']");
    private static final By REMOVE_BUTTONS = By.cssSelector("button[data-test^='remove-']");

    /**
     * Retrieves all products from the cart as a Map where key = product name, value = Product object.
     *
     * @return Map of Product objects containing name, price, and description.
     */
    public Map<String, Product> getCartProducts() {
        List<WebElement> nameElements = getElements(CART_ITEMS);
        List<WebElement> priceElements = getElements(CART_PRICES);
        List<WebElement> descriptionElements = getElements(CART_DESCRIPTIONS);

        return nameElements.stream()
                .collect(Collectors.toMap(
                        WebElement::getText,
                        nameElement -> {
                            int index = nameElements.indexOf(nameElement);
                            double price = Double.parseDouble(priceElements.get(index).getText().replace("$", "").trim());
                            String description = descriptionElements.get(index).getText().trim();
                            return new Product(nameElement.getText(), price, description);
                        }
                ));
    }

    /**
     * Validates whether all expected products are in the cart.
     *
     * @param expectedProducts List of expected Product objects.
     * @return true if all expected products match the cart items, false otherwise.
     */
    public boolean areProductsInCart(List<Product> expectedProducts) {
        Map<String, Product> cartProducts = getCartProducts();
        return expectedProducts.stream().allMatch(expected -> {
            Product cartProduct = cartProducts.get(expected.getName());
            return cartProduct != null &&
                    cartProduct.getPrice() == expected.getPrice() &&
                    cartProduct.getDescription().equals(expected.getDescription());
        });
    }
    

    public CheckoutPage proceedToCheckout() {
        click(CHECKOUT_BUTTON);
        return new CheckoutPage();
    }


}