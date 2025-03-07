import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

public class BaseTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        // Retrieve CLI or property file settings
        String browser = System.getProperty("browser"); // CLI value or fallback in DriverManager
        boolean useRemote = Boolean.parseBoolean(System.getProperty("webdriver.remote", "false"));
        DriverManager.initializeDriver(browser, useRemote);


    }
    @AfterEach
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
        }
    }
    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }
}
