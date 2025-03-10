package pages;

import org.openqa.selenium.By;
import pages.setup.BasePage;

public class CheckoutCompletePage extends BasePage {

    private static final String EXPECTED_TITLE = "Checkout: Complete!";
    private static final String EXPECTED_HEADER = "Thank you for your order!";
    private static final String EXPECTED_TEXT = "Your order has been dispatched, and will arrive just as fast as the pony can get there!";
    private By pageTitle = By.xpath("//span[@data-test='title']");
    private By completeHeader = By.xpath("//h2[@data-test='complete-header']");
    private By completeText = By.xpath("//div[@data-test='complete-text']");
    private By backToProductsButton = By.xpath("//button[@data-test='back-to-products']");

    public boolean isPageDisplayed() {
        return isDisplayed(pageTitle) && getText(pageTitle).equals(EXPECTED_TITLE);
    }

    public boolean isCompleteHeaderCorrect() {
        return getText(completeHeader).equals(EXPECTED_HEADER);
    }

    public boolean isCompleteTextCorrect() {
        return getText(completeText).equals(EXPECTED_TEXT);
    }

    public void clickBackToProducts() {
        click(backToProductsButton);
    }
}