package dtm.tuan10.base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.hamcrest.Matchers.lessThan;

public class ApiBaseTest {

    protected RequestSpecification requestSpec;
    protected ResponseSpecification responseSpec;

    /**
     * Đọc API key theo thứ tự ưu tiên:
     * 1. System property: -Dreqres.api.key=xxx (dùng khi chạy từ CI hoặc dòng lệnh)
     * 2. application.properties: reqres.api-key=xxx
     */
    private String resolveApiKey() {
        // 1. System property (override)
        String key = System.getProperty("reqres.api.key");
        if (key != null && !key.isBlank())
            return key;

        // 2. application.properties
        try (InputStream is = getClass().getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                key = props.getProperty("reqres.api-key");
                if (key != null && !key.isBlank() && !key.equals("YOUR_API_KEY_HERE"))
                    return key;
            }
        } catch (IOException ignored) {
        }

        throw new IllegalStateException(
                "Không tìm thấy reqres.in API key!\n" +
                        "  → Tạo tài khoản miễn phí tại https://app.reqres.in\n" +
                        "  → Sau đó điền key vào src/main/resources/application.properties\n" +
                        "    reqres.api-key=YOUR_ACTUAL_KEY\n" +
                        "  → Hoặc truyền khi chạy: -Dreqres.api.key=YOUR_ACTUAL_KEY");
    }

    @BeforeClass
    public void setup() {
        System.clearProperty("http.proxyHost");
        System.clearProperty("http.proxyPort");
        System.clearProperty("https.proxyHost");
        System.clearProperty("https.proxyPort");
        System.clearProperty("socksProxyHost");
        System.clearProperty("socksProxyPort");

        String apiKey = resolveApiKey();

        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://reqres.in")
                .setBasePath("/api")
                .setContentType(ContentType.JSON)
                .addHeader("Accept", "application/json")
                .addHeader("x-api-key", apiKey) // bắt buộc từ đầu 2025
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectResponseTime(lessThan(5000L))
                .build();
    }
}