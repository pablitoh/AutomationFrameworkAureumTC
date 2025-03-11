package setup;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import utils.DriverManager;
import utils.PropertyManager;
import utils.TestDataManager;

import java.io.ByteArrayInputStream;

public class BaseTest {


    private WebDriver driver;
    private JsonNode testUsers;
    private boolean testFailed = false;

    @BeforeEach
    public void setUp() {
        testUsers = TestDataManager.getTestData("users");
        // Retrieve CLI or property file settings
        String browser = System.getProperty("browser"); // CLI value or fallback in utils.DriverManager
        boolean useRemote = Boolean.parseBoolean(System.getProperty("webdriver.remote", "false"));
        DriverManager.initializeDriver(browser, useRemote);
        DriverManager.getDriver().get(PropertyManager.getProperty("app.url"));
    }

    @AfterEach
    public void tearDown() {
        try {
            byte[] screenshot = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Screenshot", "image/png", new ByteArrayInputStream(screenshot), "png");
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot in tearDown: " + e.getMessage());
        }

        if (getDriver() != null) {
            getDriver().quit();
        }
    }

    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    public JsonNode getTestUsers() {
        return testUsers;
    }


}
