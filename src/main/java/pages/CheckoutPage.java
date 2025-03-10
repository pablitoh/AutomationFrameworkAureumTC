package pages;

import models.User;
import org.openqa.selenium.By;
import pages.setup.BasePage;

public class CheckoutPage extends BasePage {

    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");

    /**
     * Fills in the checkout form using the user's information.
     *
     * @param user The user object containing first name, last name, and postal code.
     */
    public CheckoutPage fillCheckoutForm(User user) {
        sendKeys(firstNameInput, user.getFirstName());
        sendKeys(lastNameInput, user.getLastName());
        sendKeys(postalCodeInput, user.getPostalCode());
        return this;
    }

    /**
     * Submits the checkout form.
     */
    public CheckoutOverviewPage submitCheckout() {
        click(continueButton);
        return new CheckoutOverviewPage();
    }

    /**
     * Cancels the checkout process.
     */
    public void cancelCheckout() {
        click(cancelButton);
    }
}