package dtm.tuan10.api;

import dtm.tuan10.base.ApiBaseTest;
import dtm.tuan10.model.CreateUserRequest;
import dtm.tuan10.model.UserResponse;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class UserCrudApiTest extends ApiBaseTest {

    private String createdUserId;

    @Test(priority = 1)
    public void testCreateUserUsingPojo() {
        CreateUserRequest request = new CreateUserRequest("neo", "tester");

        UserResponse userResponse =
                given()
                        .spec(requestSpec)
                        .body(request)
                        .when()
                        .post("/users")
                        .then()
                        .spec(responseSpec)
                        .statusCode(201)
                        .extract()
                        .as(UserResponse.class);

        Assert.assertEquals(userResponse.getName(), "neo");
        Assert.assertEquals(userResponse.getJob(), "tester");
        Assert.assertNotNull(userResponse.getId());
        Assert.assertNotNull(userResponse.getCreatedAt());

        createdUserId = userResponse.getId();
        System.out.println("Created User ID: " + createdUserId);
    }

    @Test(priority = 2, dependsOnMethods = "testCreateUserUsingPojo")
    public void testGetCreatedUserById() {
        Response response =
                given()
                        .spec(requestSpec)
                        .when()
                        .get("/users/" + createdUserId)
                        .then()
                        .spec(responseSpec)
                        .extract()
                        .response();

        int statusCode = response.getStatusCode();

        if (statusCode == 200) {
            Assert.assertEquals(
                    response.jsonPath().getInt("data.id"),
                    Integer.parseInt(createdUserId)
            );
        } else if (statusCode == 404) {
            Assert.assertEquals(statusCode, 404);
            System.out.println("ReqRes là mock API nên GET sau POST có thể không tìm thấy user vừa tạo.");
        } else {
            Assert.fail("Status code không mong đợi: " + statusCode);
        }
    }

    @Test(priority = 3, dependsOnMethods = "testCreateUserUsingPojo")
    public void testUpdateUserWithPut() {
        CreateUserRequest request = new CreateUserRequest("neo updated", "senior tester");

        UserResponse response =
                given()
                        .spec(requestSpec)
                        .body(request)
                        .when()
                        .put("/users/" + createdUserId)
                        .then()
                        .spec(responseSpec)
                        .statusCode(200)
                        .extract()
                        .as(UserResponse.class);

        Assert.assertEquals(response.getName(), "neo updated");
        Assert.assertEquals(response.getJob(), "senior tester");
        Assert.assertNotNull(response.getUpdatedAt());
    }

    @Test(priority = 4, dependsOnMethods = "testCreateUserUsingPojo")
    public void testPartialUpdateUserWithPatch() {
        CreateUserRequest request = new CreateUserRequest(null, "lead tester");

        UserResponse response =
                given()
                        .spec(requestSpec)
                        .body(request)
                        .when()
                        .patch("/users/" + createdUserId)
                        .then()
                        .spec(responseSpec)
                        .statusCode(200)
                        .extract()
                        .as(UserResponse.class);

        Assert.assertEquals(response.getJob(), "lead tester");
        Assert.assertNotNull(response.getUpdatedAt());
    }

    @Test(priority = 5, dependsOnMethods = "testCreateUserUsingPojo")
    public void testDeleteUser() {
        given()
                .spec(requestSpec)
                .when()
                .delete("/users/" + createdUserId)
                .then()
                .spec(responseSpec)
                .statusCode(204);
    }
}