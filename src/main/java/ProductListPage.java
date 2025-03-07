import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;

public class ProductListPage extends BasePage {

    private final Header header = new Header();

    public Header getHeader() {
        return header;
    }
}
