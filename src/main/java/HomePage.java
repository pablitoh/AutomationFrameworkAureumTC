import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Represents the homepage of the website and provides methods to interact with it.
 * This class extends from the BasePage class and inherits common functionality.
 */
public class HomePage extends BasePage {
    private final Header header  = new Header();

    public HomePage goToHomePage()
    {
        String url = "http://www.amazon.es";
        getDriver().get(url);
        return this;
    }

    public ProductListPage search(String text) {
        header.search(text);
        return new ProductListPage();
    }
}
