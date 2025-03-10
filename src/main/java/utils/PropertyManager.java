package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyManager {
    private static final String ENVIRONMENT_PROPERTIES_PATH = "/testdata/%s/config.properties";
    private static final String GLOBAL_PROPERTIES_PATH = "/config.properties";
    private static Properties properties;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        properties = new Properties();

        // Load Global Properties
        try (InputStream globalProperties = PropertyManager.class.getResourceAsStream(GLOBAL_PROPERTIES_PATH)) {
            if (globalProperties != null) {
                properties.load(globalProperties);
            } else {
                throw new RuntimeException("Global config.properties not found!");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load global properties.", e);
        }

        // Load Environment-Specific properties if they exist, override global settings
        String environment = System.getProperty("env", properties.getProperty("env", "dev")).toLowerCase();
        String envPropertiesPath = String.format(ENVIRONMENT_PROPERTIES_PATH, environment);

        try (InputStream envProperties = PropertyManager.class.getResourceAsStream(envPropertiesPath)) {
            if (envProperties != null) {
                properties.load(envProperties);
                System.out.println("Loaded environment-specific properties from: " + envPropertiesPath);
            } else {
                System.out.println("No specific configuration found for " + environment + ", using global defaults.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load environment-specific properties.", e);
        }
    }

    // Get property value
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    // Get property with a default value if key is not found
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}