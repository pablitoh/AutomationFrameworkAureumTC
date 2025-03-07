import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class DriverManager {

    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";
    private static String gridUrl;
    private static boolean isRemote;
    private static String defaultBrowser;

    static {
        // Load the configuration file and allow overriding via system properties
        loadConfiguration();
    }

    /**
     * Loads the configuration from the properties file but allows overriding via CLI system properties.
     */
    private static void loadConfiguration() {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE_PATH)) {
            // Load properties from the file
            properties.load(fileInputStream);
        } catch (IOException e) {
            System.err.println("Warning: Failed to load configuration file. Default values will be used.");
        }

        // Fetch properties from the properties file, with hardcoded defaults if not defined
        gridUrl = System.getProperty("grid.url", properties.getProperty("grid.url", "http://localhost:4444"));
        isRemote = Boolean.parseBoolean(System.getProperty("webdriver.remote",
                properties.getProperty("webdriver.remote", "false")));
        defaultBrowser = System.getProperty("browser", properties.getProperty("browser", "chrome"));
    }

    /**
     * Initializes a WebDriver instance for the current thread.
     * CLI input takes precedence over configuration file values.
     *
     * @param browser   the browser to use (chrome, firefox, etc.)
     * @param useRemote whether to use a RemoteWebDriver or local WebDriver
     */
    public static void initializeDriver(String browser, Boolean useRemote) {
        if (driverThreadLocal.get() == null) {
            // Use the provided values from the arguments, otherwise use defaults
            String finalBrowser = (browser != null && !browser.isEmpty()) ? browser : defaultBrowser;
            boolean finalUseRemote = isRemote;

            WebDriver driver;
            try {
                if (finalUseRemote) {

                    // Use a remote WebDriver via Selenium Grid
                    if (finalBrowser.equalsIgnoreCase("chrome")) {
                        ChromeOptions options = new ChromeOptions();
                        options.addArguments("--no-sandbox");
                        options.addArguments("--disable-dev-shm-usage");
                        options.addArguments("--headless=new");
                        options.addArguments("--disable-gpu");
                        options.addArguments("--window-size=1920,1080");
                        driver = new RemoteWebDriver(new URL(gridUrl), options);
                    } else if (finalBrowser.equalsIgnoreCase("firefox")) {
                        FirefoxOptions options = new FirefoxOptions();
                        driver = new RemoteWebDriver(new URL(gridUrl), options);
                    } else {
                        throw new IllegalArgumentException("Unsupported browser: " + finalBrowser);
                    }
                } else {
                    // Use local WebDriver
                    switch (finalBrowser.toLowerCase()) {
                        case "chrome":
                            WebDriverManager.chromedriver().setup();
                            ChromeOptions chromeOptions = new ChromeOptions();
                            chromeOptions.addArguments("--disable-gpu");
                            chromeOptions.addArguments("--window-size=1920,1080");
                            chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                            driver = new ChromeDriver(chromeOptions);
                            break;
                        case "firefox":
                            WebDriverManager.firefoxdriver().setup();
                            FirefoxOptions firefoxOptions = new FirefoxOptions();
                            driver = new FirefoxDriver(firefoxOptions);
                            break;
                        default:
                            throw new IllegalArgumentException("Unsupported browser: " + finalBrowser);
                    }
                }
                driverThreadLocal.set(driver);

            } catch (MalformedURLException e) {
                throw new RuntimeException("Invalid Selenium Grid URL: " + gridUrl, e);
            }
        }
    }

    /**
     * Retrieves the WebDriver instance for the current thread.
     *
     * @return the WebDriver instance
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Quits the WebDriver instance for the current thread and removes it from ThreadLocal.
     */
    public static void quitDriver() {
        if (driverThreadLocal.get() != null) {
            driverThreadLocal.get().quit();
            driverThreadLocal.remove();
        }
    }
}