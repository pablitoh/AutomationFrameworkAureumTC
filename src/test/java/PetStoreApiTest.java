import com.fasterxml.jackson.databind.JsonNode;
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
    public void createPet() {
        String petName = "TestPet_" + System.currentTimeMillis(); // Generate a unique pet name
        String requestBody = "{ \"name\": \"" + petName + "\", \"status\": \"available\" }";

        String response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(RestAssured.baseURI + "/pet")
                .then()
                .statusCode(200)
                .extract().asString();

        JsonPath jsonResponse = JsonPath.from(response);
        long petId = jsonResponse.getLong("id");
        assertThat(petId).isNotNull();
        assertThat(jsonResponse.getString("name")).isEqualTo(petName);
    }

    @Test
    @Order(1)
    @DisplayName("TC-0010 | GET - Find Available Pets")

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
        System.out.println("Available pets: " + availablePetIds);
        assertThat(availablePetIds).isNotEmpty();
    }

    @Test
    @Order(2)
    @DisplayName("TC-0011 | GET - Get Pet By ID")
    public void getPetById() {
        Assumptions.assumeTrue(!availablePetIds.isEmpty(), "No available pets found");
        long petId = availablePetIds.get(0);
        System.out.println("Retrieving pet with ID: " + petId);

        String response = given()
                .when()
                .get(RestAssured.baseURI + "/pet/" + petId)
                .then()
                .statusCode(200)
                .extract().asString();

        JsonPath jsonResponse = JsonPath.from(response);
        assertThat(jsonResponse.getLong("id")).isEqualTo(petId);
    }

    @Test
    @Order(3)
    @DisplayName("TcC-0012 |PUT - Update Pet Status")
    public void updatePetStatus() {
        Assumptions.assumeTrue(!availablePetIds.isEmpty(), "No available pets found");
        long petId = availablePetIds.get(0);
        String updatedStatus = "sold";
        given()
                .contentType(ContentType.JSON)
                .body("{\"id\": " + petId + ", \"status\": \"" + updatedStatus + "\"}")
                .when()
                .put(RestAssured.baseURI + "/pet")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(4)
    @DisplayName("TC-0013 | DELETE - Remove Pet")
    public void deletePet() {
        Assumptions.assumeTrue(!availablePetIds.isEmpty(), "No available pets found");
        long petId = availablePetIds.get(0);
        System.out.println("Deleting pet with ID: " + petId);
        given()
                .when()
                .delete(RestAssured.baseURI + "/pet/" + petId)
                .then()
                .statusCode(200);
    }
}