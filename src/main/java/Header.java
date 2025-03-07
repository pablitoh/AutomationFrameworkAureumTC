import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class Header extends BasePage {

    //Locators
    private final By searchBox = By.id("twotabsearchtextbox");
    private final By searchButton = By.id("nav-search-submit-button");
    private final By headerLinks = By.xpath("//*[@id='nav-xshop']//a");


    //Actions

    /**
     * Executes a search operation by entering the specified text into a search box
     * and clicking the search button.
     *
     * @param text The text to be entered into the search box for initiating the search.
     */
    public void search(String text) {
        getElement(searchBox).sendKeys(text);
        getElement(searchButton).click();
    }

    /**
     * Clicks on a specific header link with the specified link text.
     *
     * @param linkText The visible text of the header link to be clicked.
     */
    public void clickOnHeaderLink(String linkText) {
        filterElements(getElements(headerLinks), e -> e.getText().equals(linkText)).get(0).click();
    }
}
