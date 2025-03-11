package pages.setup;


import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.DriverManager;

import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BasePage {
    private static final int DEFAULT_TIMEOUT = 10; // Default timeout in seconds
    private final WebDriver driver; // WebDriver instance

    // Constructor to initialize the WebDriver
    public BasePage() {
        this.driver = DriverManager.getDriver();
    }

    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Wait for an element to be visible and interactable before returning it (default timeout).
     *
     * @param locator The By locator of the element.
     * @return WebElement after it is visible and interactable.
     */
    public WebElement getElement(By locator) {

        Allure.step("Getting element" + locator.toString());
        return getElement(locator, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for an element to be visible and interactable before returning it (custom timeout).
     *
     * @param locator The By locator of the element.
     * @param timeout Timeout in seconds to wait for the element.
     * @return WebElement after it is visible and interactable.
     */
    public WebElement getElement(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        WebElement e = wait.until(ExpectedConditions.elementToBeClickable(locator));
        highlightElement(e);
        return e;
    }

    public List<WebElement> getElements(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        highLightElements(elements);
        return elements;
    }

    public List<WebElement> getElements(By locator) {
        return getElements(locator, DEFAULT_TIMEOUT);
    }

    // ---------- Common Actions on Elements ---------- //

    /**
     * Clicks on an element (default timeout).
     *
     * @param locator The By locator of the element.
     */
    public void click(By locator) {
        Allure.step("Clicking on element" + locator.toString());
        click(locator, DEFAULT_TIMEOUT);
    }

    /**
     * Clicks on an element (custom timeout).
     *
     * @param locator The By locator of the element.
     * @param timeout Timeout in seconds to wait for the element.
     */
    public void click(By locator, int timeout) {
        Allure.step("Clicking" + locator.toString());
        getElement(locator, timeout).click();
    }

    /**
     * Sends keys to an element (default timeout).
     *
     * @param locator The By locator of the element.
     * @param text    The text to enter into the element.
     */
    public void sendKeys(By locator, String text) {

        Allure.step("Sending keys to element" + locator.toString());
        sendKeys(locator, text, DEFAULT_TIMEOUT);
    }

    /**
     * Sends keys to an element (custom timeout).
     *
     * @param locator The By locator of the element.
     * @param text    The text to enter into the element.
     * @param timeout Timeout in seconds to wait for the element.
     */
    public void sendKeys(By locator, String text, int timeout) {
        WebElement element = getElement(locator, timeout);
        Allure.step("Sending keys to element" + locator.toString());
        element.clear(); // Clear the input field before typing
        element.sendKeys(text);
    }

    /**
     * Retrieves the visible text of an element (default timeout).
     *
     * @param locator The By locator of the element.
     * @return The visible text of the element.
     */
    public String getText(By locator) {
        return getText(locator, DEFAULT_TIMEOUT);
    }

    /**
     * Retrieves the visible text of an element (custom timeout).
     *
     * @param locator The By locator of the element.
     * @param timeout Timeout in seconds to wait for the element.
     * @return The visible text of the element.
     */
    public String getText(By locator, int timeout) {
        WebElement element = getElement(locator, timeout);
        return element.getText();
    }

    /**
     * Retrieves the value of an attribute from an element (default timeout).
     *
     * @param locator   The By locator of the element.
     * @param attribute The name of the attribute to retrieve.
     * @return The attribute value of the element.
     */
    public String getElementProperty(By locator, String attribute) {
        return getElementProperty(locator, attribute, DEFAULT_TIMEOUT);
    }

    /**
     * Retrieves the value of an attribute from an element (custom timeout).
     *
     * @param locator   The By locator of the element.
     * @param attribute The name of the attribute to retrieve.
     * @param timeout   Timeout in seconds to wait for the element.
     * @return The attribute value of the element.
     */
    public String getElementProperty(By locator, String attribute, int timeout) {
        return getElement(locator, timeout).getDomProperty(attribute);
    }

    /**
     * Checks if an element is displayed (default timeout).
     *
     * @param locator The By locator of the element.
     * @return true if the element is displayed, otherwise false.
     */
    public boolean isDisplayed(By locator) {
        return isDisplayed(locator, DEFAULT_TIMEOUT);
    }

    /**
     * Checks if an element is displayed (custom timeout).
     *
     * @param locator The By locator of the element.
     * @param timeout Timeout in seconds to wait for the element.
     * @return true if the element is displayed, otherwise false.
     */
    public boolean isDisplayed(By locator, int timeout) {
        try {
            WebElement element = getElement(locator, timeout);
            return element.isDisplayed();
        } catch (Exception e) {
            return false; // Return false if the element is not found or not displayed
        }
    }

    /**
     * Filters a list of WebElements based on the given condition.
     *
     * @param elements  The list of WebElements to filter.
     * @param condition The condition to apply as a filter
     * @return A List of WebElements that match the condition.
     */
    public List<WebElement> filterElements(List<WebElement> elements, Predicate<WebElement> condition) {
        if (elements == null || condition == null) {
            throw new IllegalArgumentException("Elements list and condition cannot be null");
        }
        return elements.stream()
                .filter(condition) // Apply the filtering condition
                .collect(Collectors.toList()); // Collect the filtered elements into a new list
    }

    private void highlightElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', 'border: 2px solid red;');", element);
    }

    private void highLightElements(List<WebElement> elements) {
        for (WebElement element : elements) {
            highlightElement(element);
        }
    }

    /**
     * Waits for the page to load and retrieves the title of the current web page.
     *
     * @return The title of the current page as a String.
     */
    public String getTitle() {
        // Wait for the page to load ("document.readyState" to become "complete")
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until((ExpectedCondition<Boolean>) wd ->
                        ((JavascriptExecutor) wd).executeScript("return document.readyState")
                                .equals("complete"));

        // Return the page title
        return getDriver().getTitle();


    }

}