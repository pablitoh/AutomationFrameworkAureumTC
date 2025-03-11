import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.*;
import utils.PropertyManager;
import utils.TestDataManager;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("Api")
@DisplayName("Pet Store API Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PetStoreApiTest {
    static List<Long> availablePetIds = new ArrayList<>(); // Static for persistence

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = PropertyManager.getProperty("api.url");
    }

    @Test
    @Order(0)
    @DisplayName("TC-0009 | API -POST - Create Pet")
    @Description("Create a pet and retrieve its ID")
    public void createPet() {
        String petName = "TestPet_" + System.currentTimeMillis(); // Generate a unique pet name
        String requestBody = "{ \"name\": \"" + petName + "\", \"status\": \"available\" }";
        Allure.step("Create a pet with name: " + petName);
        Allure.step("Request body:" + requestBody);

        String response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(RestAssured.baseURI + "/pet")
                .then()
                .statusCode(200)
                .extract().asString();
        Allure.step("Response body:" + response);

        JsonPath jsonResponse = JsonPath.from(response);
        long petId = jsonResponse.getLong("id");
        assertThat(petId).isNotNull();
        assertThat(petId).isGreaterThan(0);
        assertThat(jsonResponse.getString("name")).isEqualTo(petName);
        assertThat(jsonResponse.getString("status")).isEqualTo("available");
    }

    @Test
    @Order(1)
    @DisplayName("TC-0010 | GET - Find Available Pets")
    @Description("Find all available pets")
    public void findAvailablePets() {
        JsonNode testData = TestDataManager.getTestData("petdata");
        String response = given()
                .header("Authorization", testData.get("authToken").asText())
                .queryParam("status", "available")
                .when()
                .get(RestAssured.baseURI + "/pet/findByStatus")
                .then()
                .statusCode(200)
                .extract().asString();

        JsonPath jsonResponse = JsonPath.from(response);
        availablePetIds = jsonResponse.getList("id", Long.class);
        Allure.step("Available pets: " + availablePetIds);
        assertThat(availablePetIds).isNotEmpty();
    }

    @Test
    @Order(2)
    @DisplayName("TC-0011 | GET - Get Pet By ID")
    @Description("Retrieve a pet by its ID")
    public void getPetById() {
        Assumptions.assumeTrue(!availablePetIds.isEmpty(), "No available pets found");
        long petId = availablePetIds.get(0);
        Allure.step("Retrieving pet with ID: " + petId);
        String response = given()
                .when()
                .get(RestAssured.baseURI + "/pet/" + petId)
                .then()
                .statusCode(200)
                .extract().asString();
        Allure.step("Response body:" + response);
        JsonPath jsonResponse = JsonPath.from(response);
        assertThat(jsonResponse.getLong("id")).isEqualTo(petId);
    }

    @Test
    @Order(3)
    @DisplayName("TcC-0012 |PUT - Update Pet Status")
    @Description("Update the status of a pet")
    public void updatePetStatus() {
        Assumptions.assumeTrue(!availablePetIds.isEmpty(), "No available pets found");
        long petId = availablePetIds.get(0);
        String updatedStatus = "sold";
        String body = "{\"id\":" + petId + ",\"status\":\"" + updatedStatus + "\"}";

        Allure.step("Retrieving pet with ID: " + petId);
        Allure.step("Request body:" + body);
        Allure.step("Updating pet status to: " + updatedStatus);

        String response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put(RestAssured.baseURI + "/pet")
                .then()
                .statusCode(200).extract().asString();

        Allure.step("Response body:" + response);
        JsonPath jsonResponse = JsonPath.from(response);
        assertThat(jsonResponse.getLong("id")).isEqualTo(petId);
        assertThat(jsonResponse.getString("status")).isEqualTo(updatedStatus);
    }

    @Test
    @Order(4)
    @DisplayName("TC-0013 | DELETE - Remove Pet")
    @Description("Delete a pet by its ID")
    public void deletePet() {
        Assumptions.assumeTrue(!availablePetIds.isEmpty(), "No available pets found");
        long petId = availablePetIds.get(0);
        Allure.step("Deleting pet with ID: " + petId);
        String response = given()
                .when()
                .delete(RestAssured.baseURI + "/pet/" + petId)
                .then()
                .statusCode(200).extract().asString();
        Allure.step("Response body:" + response);
    }
}