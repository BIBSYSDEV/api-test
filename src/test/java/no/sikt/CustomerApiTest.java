package no.sikt;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import io.qameta.allure.restassured.AllureRestAssured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CustomerApiTest {

    private static final String BASE_URL = "https://api.e2e.nva.aws.unit.no";
    private static final String CUSTOMER_ID = "702c4fbe-d51a-4d20-aec8-50beb813ff36";

    private static final String ACCESS_TOKEN = CognitoLogin.login("test-user-project-manager@test.no")
            .get("accessToken");

    // @Test
    public void testGetCustomerResponse() {
        given()
            .filter(new AllureRestAssured())
            .auth().oauth2(ACCESS_TOKEN)
            .baseUri(BASE_URL)
            .contentType(ContentType.JSON)
        .when()
            .get("/customer/" + CUSTOMER_ID)
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)

            // Validate basic fields
            .body("id", equalTo(BASE_URL + "/customer/" + CUSTOMER_ID))
            .body("identifier", equalTo(CUSTOMER_ID))
            .body("type", equalTo("Customer"))
            .body("@context", equalTo("https://bibsysdev.github.io/src/customer-context.json"))

            // Validate customer details
            .body("name", equalTo("Universitetet i Sørøst-Norge"))
            .body("displayName", equalTo("USN"))
            .body("shortName", equalTo("USN"))
            .body("archiveName", equalTo("USN"))
            .body("cristinId", equalTo(BASE_URL + "/cristin/organization/222.0.0.0"))
            .body("customerOf", equalTo("nva.unit.no"))

            // Validate dates
            .body("createdDate", notNullValue())
            .body("modifiedDate", notNullValue())

            // Validate vocabularies
            .body("vocabularies", hasSize(2))
            .body("vocabularies[0].type", equalTo("Vocabulary"))
            .body("vocabularies[0].name", equalTo("HRCS Activity"))
            .body("vocabularies[0].id", equalTo("https://nva.unit.no/hrcs/activity"))
            .body("vocabularies[0].status", equalTo("Default"))
            .body("vocabularies[1].name", equalTo("HRCS Category"))
            .body("vocabularies[1].id", equalTo("https://nva.unit.no/hrcs/category"))
            .body("vocabularies[1].status", equalTo("Allowed"))

            // Validate workflow and flags
            .body("publicationWorkflow", equalTo("RegistratorPublishesMetadataOnly"))
            .body("nviInstitution", equalTo(true))
            .body("rboInstitution", equalTo(true))
            .body("generalSupportEnabled", equalTo(true))
            .body("sector", equalTo("UHI"))
            .body("autoPublishScopusImportFiles", equalTo(false))

            // Validate DOI agent
            .body("doiAgent.id", equalTo(BASE_URL + "/customer/" + CUSTOMER_ID + "/doiagent"))
            .body("doiAgent.url", equalTo("mds.test.datacite.org"))
            .body("doiAgent.prefix", equalTo("10.15157"))
            .body("doiAgent.username", equalTo("NVATEST.UNIT"))

            // Validate rights retention strategy
            .body("rightsRetentionStrategy.type", equalTo("NullRightsRetentionStrategy"))

            // Validate empty collections
            .body("serviceCenter", anEmptyMap())
            .body("channelClaims", empty())

            // Validate allowFileUploadForTypes contains expected values
            .body("allowFileUploadForTypes", hasSize(60))
            .body("allowFileUploadForTypes", hasItems(
                    "AcademicArticle",
                    "AcademicChapter",
                    "DataSet",
                    "DegreeMaster",
                    "DegreePhd",
                    "VisualArts"));
    }

    // @Test
    public void testGetCustomerResponse_DetailedVocabularyValidation() {
        given()
            .filter(new AllureRestAssured())
            .auth().oauth2(ACCESS_TOKEN)
            .baseUri(BASE_URL)
            .contentType(ContentType.JSON)
        .when()
            .get("/customer/" + CUSTOMER_ID)
        .then()
            .statusCode(200)
            .body("vocabularies.findAll { it.status == 'Default' }.size()", equalTo(1))
            .body("vocabularies.findAll { it.status == 'Allowed' }.size()", equalTo(1))
            .body("vocabularies*.name", containsInAnyOrder("HRCS Activity", "HRCS Category"));
    }

    // @Test
    public void testGetCustomerResponse_FileUploadTypesValidation() {
        given()
            .filter(new AllureRestAssured())
            .auth().oauth2(ACCESS_TOKEN)
            .baseUri(BASE_URL)
            .contentType(ContentType.JSON)
        .when()
            .get("/customer/" + CUSTOMER_ID)
        .then()
            .statusCode(200)
            .body("allowFileUploadForTypes", hasSize(60));
    }
}