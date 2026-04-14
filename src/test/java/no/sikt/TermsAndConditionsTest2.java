package no.sikt;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;

import io.qameta.allure.restassured.AllureRestAssured;

import static org.hamcrest.Matchers.*;

public class TermsAndConditionsTest2 {
    

    
    // @Test
    public void testGetTermsAndConditions() {
        RestAssured.baseURI = "https://api.e2e.nva.aws.unit.no";

        given()
            .filter(new AllureRestAssured())
        .when()
            .log().all()
            .get("users-roles/terms-and-conditions/current")
        .then()
            .log().all()
            .statusCode(200)
            .contentType("application/json")
            .body("$", not(empty()))
            .body("termsConditionsUri", equalTo("https://nva.sikt.no/terms/2024-10-01"));
    }
    
}
