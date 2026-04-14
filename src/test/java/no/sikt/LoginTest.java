package no.sikt;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;

public class LoginTest {

    private static final String BASE_URI = "https://api.e2e.nva.aws.unit.no";
    private static final String TEST_CUSTOMER_ID = "702c4fbe-d51a-4d20-aec8-50beb813ff36"; // Example customer ID
    private String accessToken;

    // @BeforeEach
    void setUp() throws Exception {
        RestAssured.baseURI = BASE_URI;
        Map<String, String> tokens = CognitoLogin.login("test-user-project-manager@test.no");
        accessToken = tokens.get("accessToken");
    }

    // @Test
    public void testLogin() throws Exception {
        Map<String, String> tokens = CognitoLogin.login("test-user-project-manager@test.no");
        assert tokens.get("accessToken") != null;
    }

    // @Test
    void testGetCustomerById() {
        given()
                .filter(new AllureRestAssured())
                .auth().oauth2(accessToken)
                .when()
                .get("/customer/702c4fbe-d51a-4d20-aec8-50beb813ff36")
                .then()
                .log().all()
                .statusCode(200)
                .contentType("application/json")
                .body("identifier", notNullValue())
                .body("id", equalTo("https://api.e2e.nva.aws.unit.no/customer/" + TEST_CUSTOMER_ID))
                .body("createdDate", notNullValue())
                .body("displayName", notNullValue())
                .body("shortName", notNullValue())
                .body("archiveName", notNullValue())
                .body("christinId", notNullValue())
                .body("feideOrganizationDomain", notNullValue());
    }

    // @Test
    void testGetNonExistentCustomer() {
        String randomUuid = UUID.randomUUID().toString();

        given()
                .filter(new AllureRestAssured())
                .auth().oauth2(accessToken)
                .log().all()
                .when()
                .get(String.format("/customer/%s", randomUuid))
                .then()
                .log().all()
                .statusCode(404);
    }
}