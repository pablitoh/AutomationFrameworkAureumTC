package pages;

import models.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import pages.setup.BasePage;

import java.util.List;
import java.util.stream.Collectors;

public class ProductListPage extends BasePage {

    // Generic Locator
    private static final String ADD_TO_CART_BUTTON_TEMPLATE = "//div[@data-test='inventory-item-name' and text()='%s']/ancestor::div[@class='inventory_item']//button[contains(@data-test, 'add-to-cart')]";
    // Locators
    private final By sortDropdown = By.cssSelector("select[data-test='product-sort-container']");
    private final By inventoryItems = By.cssSelector("div[data-test='inventory-item']");
    private final By inventoryItemNames = By.cssSelector("div[data-test='inventory-item-name']");
    private final By inventoryItemPrices = By.cssSelector("div[data-test='inventory-item-price']");
    private final By addToCartButtons = By.cssSelector("button[data-test^='add-to-cart-']");
    private final By shoppingCartLink = By.cssSelector("a[data-test='shopping-cart-link']");
    private final By pageTitle = By.cssSelector("span[data-test='title']");
    private Header header;
    private Footer footer;

    public ProductListPage() {
        super();
        this.header = new Header();
        this.footer = new Footer();
    }

    /**
     * Selects a sorting option from the dropdown.
     *
     * @param sortOption The visible text of the sorting option (e.g., "Price (low to high)").
     */
    public ProductListPage selectSortOption(String sortOption) {
        WebElement dropdown = getElement(sortDropdown);
        Select select = new Select(dropdown);
        select.selectByVisibleText(sortOption);
        return this;
    }

    /**
     * Retrieves all product names on the product list page.
     *
     * @return A list of product names.
     */
    public List<String> getProductNames() {
        return getElements(inventoryItemNames)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all product prices as double values.
     *
     * @return A list of product prices.
     */
    public List<Double> getProductPrices() {
        return getElements(inventoryItemPrices)
                .stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .collect(Collectors.toList());
    }

    /**
     * Adds all available products to the cart.
     */
    public void addAllItemsToCart() {
        List<WebElement> buttons = getElements(addToCartButtons);
        for (WebElement button : buttons) {
            button.click();
        }
    }

    /**
     * Checks if all important elements are displayed on the product list page.
     *
     * @return true if all elements are visible, false otherwise.
     */
    public boolean areAllImportantElementsDisplayed() {
        return isDisplayed(pageTitle) &&
                isDisplayed(sortDropdown) &&
                isDisplayed(inventoryItems) &&
                isDisplayed(shoppingCartLink);
    }

    /**
     * Retrieves the details of a product based on its name.
     *
     * @param productName The name of the product.
     * @return A `Product` object containing name, price, and description.
     */
    public Product getProductDetails(String productName) {
        By productNameLocator = By.xpath("//div[@data-test='inventory-item-name' and text()='" + productName + "']");
        By productPriceLocator = By.xpath("//div[@data-test='inventory-item-name' and text()='" + productName + "']/ancestor::div[@class='inventory_item']//div[@data-test='inventory-item-price']");
        By productDescLocator = By.xpath("//div[@data-test='inventory-item-name' and text()='" + productName + "']/ancestor::div[@class='inventory_item']//div[@data-test='inventory-item-desc']");

        String name = getText(productNameLocator);
        double price = Double.parseDouble(getText(productPriceLocator).replace("$", "").trim());
        String description = getText(productDescLocator).trim();

        return new Product(name, price, description);
    }

    /**
     * Adds a single product to the cart.
     *
     * @param productName The name of the product.
     */
    public void addProductToCart(String productName) {
        By addToCartButton = By.xpath(String.format(ADD_TO_CART_BUTTON_TEMPLATE, productName));
        click(addToCartButton);
    }

    /**
     * Adds multiple products to the cart.
     *
     * @param productNames A list of product names.
     */
    public ProductListPage addProductsToCart(List<String> productNames) {
        for (String productName : productNames) {
            addProductToCart(productName);
        }
        return this;
    }

    public Header getHeader() {
        return header;
    }

    public Footer getFooter() {
        return footer;
    }
}