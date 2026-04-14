package no.sikt;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;

public class CountryApiTest {

    // @Test
    public void testGetCountries() {
        RestAssured.baseURI = "https://api.e2e.nva.aws.unit.no";

        given()
            .filter(new AllureRestAssured())
        .when()
            .get("/cristin/country")
        .then()
            .statusCode(200)
            .contentType("application/json")
            .body("$", not(empty()))
            .body("size()", greaterThan(0));
    }

//     @Test
    public void testCountryDataStructure() {
        RestAssured.baseURI = "https://api.e2e.nva.aws.unit.no";

        given()
            .filter(new AllureRestAssured())
            .baseUri("https://api.e2e.nva.aws.unit.no")
        .when()
            .get("/cristin/country")
        .then()
            .log().all()
            .statusCode(200)
            .contentType("application/json")
            .body("hits[0].identifier", notNullValue())
            .body("hits[0].identifierAlpha3", notNullValue())
            .body("hits[0].labels", notNullValue());
    }
}