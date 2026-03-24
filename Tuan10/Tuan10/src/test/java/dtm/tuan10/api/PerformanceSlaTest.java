package dtm.tuan10.api;

import dtm.tuan10.base.ApiBaseTest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Bài 5: PERFORMANCE ASSERTION VÀ SLA MONITORING
 */
public class PerformanceSlaTest extends ApiBaseTest {

    @DataProvider(name = "slaData")
    public Object[][] slaData() {
        return new Object[][] {
                // Method, Endpoint, SLA (maxMs), Expected Status, JsonPath, Matcher
                { "GET", "/users", 2000, 200, "data.size()", greaterThanOrEqualTo(1) },
                { "GET", "/users/2", 1500, 200, "data.id", equalTo(2) },
                { "POST", "/users", 3000, 201, "id", notNullValue() },
                { "POST", "/login", 2000, 200, "token", notNullValue() },
                { "DELETE", "/users/2", 1000, 204, null, null }
        };
    }

    @Test(dataProvider = "slaData", description = "Giám sát SLA và Performance Assertion cho các endpoint chính")
    public void testSlaMonitoring(String method, String endpoint, int maxMs, int status, String jsonPath,
            Object matcher) {
        Response response = callApiWithSla(method, endpoint, maxMs);
        long responseTime = response.getTime();

        // Log kết quả ra console theo yêu cầu
        System.out.println(String.format("[SLA MONITORING] %-6s %-15s | Thực tế: %4dms | SLA: %4dms | Status: %d",
                method, endpoint, responseTime, maxMs, response.getStatusCode()));

        // Assertions
        response.then().statusCode(status);
        response.then().time(lessThan((long) maxMs));

        if (jsonPath != null && matcher != null) {
            response.then().body(jsonPath, (org.hamcrest.Matcher<?>) matcher);
        }
    }

    @Step("Gọi {method} {endpoint} - SLA: {maxMs}ms")
    private Response callApiWithSla(String method, String endpoint, int maxMs) {
        RequestSpecification spec = given(requestSpec);

        // Mock data cho các request POST
        if (method.equalsIgnoreCase("POST")) {
            if (endpoint.contains("login")) {
                spec.body(Map.of("email", "eve.holt@reqres.in", "password", "cityslicka"));
            } else {
                spec.body(Map.of("name", "performance tester", "job", "tester"));
            }
        }

        return switch (method.toUpperCase()) {
            case "GET" -> spec.get(endpoint);
            case "POST" -> spec.post(endpoint);
            case "DELETE" -> spec.delete(endpoint);
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        };
    }

    @Test(description = "Mô phỏng monitoring: Chạy lặp lại 10 lần và tính toán thống kê hiệu năng")
    public void testPerformanceMonitoringLoop10Times() {
        String endpoint = "/users";
        List<Long> times = new ArrayList<>();

        System.out.println("\n--- BẮT ĐẦU MONITORING 10 LẦN endpoint: " + endpoint + " ---");

        for (int i = 1; i <= 10; i++) {
            Response response = given(requestSpec)
                    .queryParam("page", 1)
                    .when()
                    .get(endpoint);

            long time = response.getTime();
            times.add(time);
            System.out.println("Lần " + i + ": " + time + "ms");
        }

        long min = Collections.min(times);
        long max = Collections.max(times);
        double avg = times.stream().mapToLong(Long::longValue).average().orElse(0.0);

        System.out.println("--------------------------------------------------");
        System.out.println("KẾT QUẢ THỐNG KÊ (MONITORING STATS):");
        System.out.println(" - Min Response Time: " + min + "ms");
        System.out.println(" - Max Response Time: " + max + "ms");
        System.out.println(" - Avg Response Time: " + String.format("%.2f", avg) + "ms");
        System.out.println("--------------------------------------------------\n");

        Assert.assertTrue(avg < 1500, "SLA VIOLATION: Thời gian phản hồi trung bình (" + avg + "ms) vượt quá 1500ms");
    }
}
