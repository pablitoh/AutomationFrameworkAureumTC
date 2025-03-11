package pages;

import io.qameta.allure.Allure;
import models.User;
import org.openqa.selenium.By;
import pages.setup.BasePage;

public class LandingPage extends BasePage {
    By usernameInput = By.cssSelector("input[data-test='username']");
    By passwordInput = By.cssSelector("input[data-test='password']");
    By loginButton = By.cssSelector("input[data-test='login-button']");
    By errorMessageContainer = By.cssSelector("div.error-message-container");

    public LandingPage goToHome() {
        getDriver().get("https://www.saucedemo.com/");
        return this;
    }

    //methods to fill the form and submit
    public void fillLoginForm(String username, String password) {
        Allure.step("Fill login form with username: " + username + " and password: " + password);
        getElement(usernameInput).sendKeys(username);
        getElement(passwordInput).sendKeys(password);
        getElement(loginButton).click();
    }

    //get error message
    public String getErrorMessage() {
        return getElement(errorMessageContainer).getText();
    }

    public ProductListPage login(User user) {
        Allure.step("Login with username: " + user.getUsername() + " and password: " + user.getPassword());
        fillLoginForm(user.getUsername(), user.getPassword());
        return new ProductListPage();
    }


}
