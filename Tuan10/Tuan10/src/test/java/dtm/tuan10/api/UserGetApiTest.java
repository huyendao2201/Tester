package dtm.tuan10.api;

import dtm.tuan10.base.ApiBaseTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserGetApiTest extends ApiBaseTest {

    @Test(description = "GET /users?page=1 - status 200, page=1, total_pages > 0, data.size >= 1")
    public void testGetUsersPage1() {
        given(requestSpec)
                .queryParam("page", 1)
                .when()
                .get("/users")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("page", equalTo(1))
                .body("total_pages", greaterThan(0))
                .body("data.size()", greaterThanOrEqualTo(1));
    }

    @Test(description = "GET /users?page=2 - page=2, mỗi user có id, email, first_name, last_name, avatar")
    public void testGetUsersPage2ValidateEachUserFields() {
        given(requestSpec)
                .queryParam("page", 2)
                .when()
                .get("/users")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("page", equalTo(2))
                .body("data", not(empty()))
                .body("data.id", everyItem(notNullValue()))
                .body("data.email", everyItem(allOf(notNullValue(), containsString("@"))))
                .body("data.first_name", everyItem(allOf(notNullValue(), not(emptyString()))))
                .body("data.last_name", everyItem(allOf(notNullValue(), not(emptyString()))))
                .body("data.avatar", everyItem(allOf(notNullValue(), startsWith("http"))));
    }

    @Test(description = "GET /users/3 - id=3, email đúng định dạng @reqres.in, first_name không rỗng")
    public void testGetUserById3() {
        given(requestSpec)
                .when()
                .get("/users/3")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("data.id", equalTo(3))
                .body("data.email", allOf(notNullValue(), endsWith("@reqres.in")))
                .body("data.first_name", allOf(notNullValue(), not(emptyOrNullString())));
    }

    @Test(description = "GET /users/9999 - status 404, body là object rỗng")
    public void testGetUserNotFound9999() {
        given(requestSpec)
                .when()
                .get("/users/9999")
                .then()
                .spec(responseSpec)
                .statusCode(404)
                .body("$", anyOf(anEmptyMap(), nullValue()));
    }
}