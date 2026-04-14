package no.sikt;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

public class TermsAndConditionsTest1 {
    
    // @Test
    public void testGetTermsAndConditions() {
        // set base URI for API
        given()
            .baseUri("https://api.e2e.nva.aws.unit.no")
        .when()
            .log().all()
            .get("users-roles/terms-and-conditions/current")
        // validate response
        .then()
            .log().all()
            .statusCode(200)
            .contentType("application/json")
            .body("$", not(empty()))
            .body("termsConditionsUri", equalTo("https://nva.sikt.no/terms/2024-10-01"));
    }
    
}
