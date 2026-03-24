package dtm.tuan10.api;

import dtm.tuan10.base.ApiBaseTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

/**
 * Bài 3: JSON Schema Validation
 */
public class JsonSchemaValidationTest extends ApiBaseTest {

    // TEST 1: GET /api/users → user-list-schema.json
    @Test(priority = 1, description = "Schema: GET /api/users?page=1 phải khớp user-list-schema.json " +
            "(page, per_page, total, total_pages, data[], support; additionalProperties:false)")
    public void testUserListSchemaValidation() {
        given(requestSpec)
                .queryParam("page", 1)
                .when()
                .get("/users")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/user-list-schema.json"));
    }

    // TEST 2: GET /api/users/2 → user-schema.json
    @Test(priority = 2, description = "Schema: GET /api/users/2 phải khớp user-schema.json " +
            "(nested data object + support; additionalProperties:false)")
    public void testSingleUserSchemaValidation() {
        given(requestSpec)
                .when()
                .get("/users/2")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"));
    }

    // TEST 3: POST /api/users → create-user-schema.json
    @Test(priority = 3, description = "Schema: POST /api/users phải khớp create-user-schema.json " +
            "(name, job, id, createdAt; additionalProperties:false)")
    public void testCreateUserSchemaValidation() {
        String requestBody = """
                {
                    "name": "neo",
                    "job":  "tester"
                }
                """;

        given(requestSpec)
                .body(requestBody)
                .when()
                .post("/users")
                .then()
                .spec(responseSpec)
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/create-user-schema.json"));
    }

    // TEST 4 — DEMO: Schema Violation Detection
    @Test(priority = 4, description = "DEMO: additionalProperties:false bắt được field 'per_page', 'total', 'support' "
            +
            "không khai báo trong schema → REST Assured ném AssertionError (PASSED = cơ chế hoạt động đúng)", expectedExceptions = AssertionError.class)
    public void testDemoSchemaFailsWhenResponseHasUndeclaredFields() {
        given(requestSpec)
                .queryParam("page", 1)
                .when()
                .get("/users")
                .then()
                .body(matchesJsonSchemaInClasspath("schemas/user-list-schema-strict-demo.json"));
    }
}
