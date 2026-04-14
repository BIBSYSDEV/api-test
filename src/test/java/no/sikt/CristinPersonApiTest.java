package no.sikt;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;


public class CristinPersonApiTest {
    
    private static String personPayload = "{\"identifiers\":[{\"type\":\"NationalIdentificationNumber\",\"value\":\"\"}],\"names\":[{\"type\":\"FirstName\",\"value\":\"API\"},{\"type\":\"LastName\",\"value\":\"Tester\"}],\"employments\":[{\"type\":\"https://api.e2e.nva.aws.unit.no/cristin/position#1020\",\"organization\":\"https://api.e2e.nva.aws.unit.no/cristin/organization/20202.0.0.0\",\"startDate\":\"2026-03-31T22:00:00.000Z\",\"endDate\":\"\",\"fullTimeEquivalentPercentage\":\"\"}]}";
    private static final String ACCESS_TOKEN = CognitoLogin.login("test-user-editor@test.no").get("accessToken");
    
    @BeforeAll
    public static void setUp(){
        RestAssured.filters(new AllureRestAssured());
    }
    
    
    @Test
    public void testCreate() {
        System.out.println("test create person");
        given()
            // .filter(new AllureRestAssured())
            .auth().oauth2(ACCESS_TOKEN)
            .baseUri("https://api.e2e.nva.aws.unit.no")
            .body(personPayload)
        .when()
            .post("/cristin/person")
        .then()
            .statusCode(201)
            .time(lessThan(6000L))
            .body("id", notNullValue())
            .body("identifiers", notNullValue())
            .body("names", notNullValue())
            .body("affiliations", notNullValue())
            .body("employments", notNullValue());
    }
}
