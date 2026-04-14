package no.sikt;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import io.qameta.allure.restassured.AllureRestAssured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class PublicationApiTest {

    private static final String BASE_URI = "https://api.e2e.nva.aws.unit.no";
    private static String PUBLICATION_ID = "019a293df98f-5e2f5aee-65af-4c22-9779-e79bbd584685";
    private static final String ACCESS_TOKEN = CognitoLogin.login("test-user-nvi@test.no")
        .get("accessToken");

    
    // @Test
    public void testGetPublication() {
        given()
            .log().all()
            .filter(new AllureRestAssured())
            .baseUri(BASE_URI)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
        .when()
            .log().all()
        .get("/publication/" + PUBLICATION_ID)
        .then()
            .statusCode(200);
    }
}
