package dtm.tuan10.api;

import dtm.tuan10.base.ApiBaseTest;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Bài 4: KIỂM THỬ AUTHORIZATION VÀ ERROR HANDLING
 */
public class AuthAndErrorTest extends ApiBaseTest {

    // PHẦN A - AUTHORIZATION (LOGIN & REGISTER)

    @Test(priority = 1, description = "Test login thành công: 200, có token")
    public void testLoginSuccess() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "cityslicka");

        given(requestSpec)
                .body(body)
                .when()
                .post("/login")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("token", not(emptyOrNullString()));
    }

    @Test(priority = 2, description = "Test register thành công: 200, có id và token")
    public void testRegisterSuccess() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "pistol");

        given(requestSpec)
                .body(body)
                .when()
                .post("/register")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("id", notNullValue())
                .body("token", not(emptyOrNullString()));
    }

    @Test(priority = 3, description = "Test register thiếu password: 400, 'Missing password'")
    public void testRegisterMissingPassword() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "sydney@fife");
        // Không truyền password

        given(requestSpec)
                .body(body)
                .when()
                .post("/register")
                .then()
                .spec(responseSpec)
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    // PHẦN B - DATA-DRIVEN CHO ERROR HANDLING (LOGIN)

    @DataProvider(name = "loginScenarios")
    public Object[][] loginScenarios() {
        return new Object[][] {
                // email, password, expectedStatus, expectedError
                { "eve.holt@reqres.in", "cityslicka", 200, null },
                { "eve.holt@reqres.in", "", 400, "Missing password" },
                { "", "cityslicka", 400, "Missing email or username" },
                { "notexist@reqres.in", "wrongpass", 400, "user not found" },
                { "invalid-email", "pass123", 400, "user not found" },
        };
    }

    @Test(priority = 4, dataProvider = "loginScenarios", description = "Data-Driven login scenarios (Error Handling)")
    public void testLoginScenarios(String email, String password, int expectedStatus, String expectedError) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);

        // Chỉ add password nếu không rỗng (để test trường hợp "Missing password")
        if (password != null && !password.isEmpty()) {
            body.put("password", password);
        }

        ValidatableResponse response = given(requestSpec)
                .body(body)
                .when()
                .post("/login")
                .then()
                .statusCode(expectedStatus);

        if (expectedError != null) {
            response.body("error", containsString(expectedError));
        } else {
            // Nếu thành công thì verify có token
            response.body("token", notNullValue());
        }
    }
}
