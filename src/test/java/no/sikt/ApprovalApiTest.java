package no.sikt;

import static io.restassured.RestAssured.*;

import org.junit.jupiter.api.Test;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static org.hamcrest.Matchers.*;

public class ApprovalApiTest {
    
    private static final String BASE_URL = "https://api.e2e.nva.aws.unit.no";

    private static final String ACCESS_TOKEN = CognitoLogin.login("test-user-editor@test.no")
            .get("accessToken");


    // @Test
    public void testCreate() {
        RestAssured.baseURI = BASE_URL;
        given()
            .filter(new AllureRestAssured())
            .auth().oauth2(ACCESS_TOKEN)
            .baseUri(BASE_URL)
            .contentType(ContentType.JSON)
        .when()
            .post("/approval")
        .then()
            .log().all()
            .statusCode(200)
            .contentType("application/json")
            .body("$", not(empty()))
            .body("size()", greaterThan(0));
    }
}
