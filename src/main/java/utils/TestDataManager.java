package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.User;
import models.UserType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TestDataManager {

    private static final String GLOBAL_PATH = "/testdata/%s.json";
    private static final String ENV_PATH = "/testdata/%s/%s.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String environment = System.getProperty("env", "dev").toLowerCase();

    /**
     * Retrieves a User object based on the given UserType.
     *
     * @param userType The UserType enum value
     * @return User object containing test data
     */
    public static User getUser(UserType userType) {
        JsonNode usersJson = getTestData("users");
        if (usersJson != null && usersJson.has(userType.getKey())) {
            return mapper.convertValue(usersJson.get(userType.getKey()), User.class);
        }
        throw new RuntimeException("User data not found for type: " + userType);
    }

    /**
     * Retrieves JSON test data for the given file name, merging environment-specific and global data.
     *
     * @param jsonFileName The base file name (without extension)
     * @return JsonNode containing merged test data
     */
    public static JsonNode getTestData(String jsonFileName) {
        return loadAndMergeData(jsonFileName);
    }

    /**
     * Loads and merges global and environment-specific JSON data.
     *
     * @param fileName The name of the JSON file (without extension)
     * @return JsonNode containing merged data
     */
    private static JsonNode loadAndMergeData(String fileName) {
        try {
            JsonNode globalData = loadJsonFromFile(String.format(GLOBAL_PATH, fileName));

            // If global data is null (file not found), initialize an empty JSON object
            if (globalData == null) {
                System.out.println("Global JSON for '" + fileName + ".json' NOT found, initializing with empty data.");
                globalData = mapper.createObjectNode();
            }

            // Load environment-specific data (overrides global if exists)
            JsonNode envData = loadJsonFromFile(String.format(ENV_PATH, environment, fileName));

            if (envData != null) {
                merge((ObjectNode) globalData, envData);
            } else {
                System.out.println("No environment-specific JSON found for environment " + environment + ", using global defaults.");
            }

            return globalData;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load and merge JSON data for: " + fileName, e);
        }
    }

    /**
     * Loads JSON data from the specified file path.
     *
     * @param path The file path
     * @return JsonNode representing the JSON structure
     */
    private static JsonNode loadJsonFromFile(String path) {
        try (InputStream input = TestDataManager.class.getResourceAsStream(path)) {
            if (input != null) {
                return mapper.readTree(input);
            }
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + path);
        }
        return null;
    }

    /**
     * Merges two JSON nodes recursively. The environment-specific JSON overrides global JSON.
     *
     * @param globalNode The base JSON object
     * @param envNode    The overriding JSON object
     */
    private static void merge(ObjectNode globalNode, JsonNode envNode) {
        envNode.fields().forEachRemaining(entry -> {
            JsonNode globalField = globalNode.get(entry.getKey());
            if (globalField != null && globalField.isObject() && entry.getValue().isObject()) {
                merge((ObjectNode) globalField, entry.getValue());
            } else {
                globalNode.replace(entry.getKey(), entry.getValue());
            }
        });
    }

    public static List<String> getAllProductNames() {
        return getTestData("products").get("products").findValuesAsText("name");
    }

    public static List<String> getThreeRandomProductNames() {
        List<String> allProducts = getAllProductNames();
        Collections.shuffle(allProducts);
        return allProducts.stream().limit(3).collect(Collectors.toList());
    }
}