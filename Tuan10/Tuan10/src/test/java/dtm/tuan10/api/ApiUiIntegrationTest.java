package dtm.tuan10.api;

import dtm.tuan10.base.ApiBaseTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Bài 6: TÍCH HỢP API + UI: SETUP QUA API, VERIFY QUA UI
 */
public class ApiUiIntegrationTest extends ApiBaseTest {

    private WebDriver driver;
    private String apiToken;
    private boolean isApiAlive = false;

    @BeforeClass
    public void setupWdm() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    public void initDriverAndPrecondition() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // API Precondition logic
        System.out.println("\n[API Precondition] Đang xác thực qua API reqres.in...");
        Response response = given(requestSpec)
                .body(Map.of("email", "eve.holt@reqres.in", "password", "cityslicka"))
                .when()
                .post("/login");

        if (response.getStatusCode() == 200) {
            apiToken = response.jsonPath().getString("token");
        } else {
            apiToken = null;
        }
    }

    private void loginSauceDemoUI(String user, String pass) {
        System.out.println("[UI Action] Truy cập saucedemo.com và đăng nhập...");
        driver.get("https://www.saucedemo.com/");

        driver.findElement(By.id("user-name")).sendKeys(user);
        driver.findElement(By.id("password")).sendKeys(pass);
        driver.findElement(By.id("login-button")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("inventory"));
    }

    // PHẦN A - API PRECONDITION → UI VERIFICATION

    @Test(priority = 1, description = "Phần A: Kiểm tra UI Saucedemo chỉ khi API Login thành công")
    public void testUiLoginWithApiPrecondition() {
        if (apiToken == null) {
            throw new SkipException("API Precondition login thất bại, bỏ qua test UI.");
        }
        System.out.println("[API Precondition] OK. Token: " + apiToken);

        loginSauceDemoUI("standard_user", "secret_sauce");

        String currentUrl = driver.getCurrentUrl();
        String pageTitle = driver.getTitle();
        System.out.println("[UI Verify] URL: " + currentUrl + " | Title: " + pageTitle);

        Assert.assertTrue(currentUrl.contains("inventory"));
        Assert.assertEquals(pageTitle, "Swag Labs");
    }

    // PHẦN B - LUỒNG TÍCH HỢP ĐẦY ĐỦ

    @Test(priority = 2, description = "Phần B: Kiểm tra hệ thống sống qua API và thực hiện luồng trên UI")
    public void testFullIntegrationFlow() {

        System.out.println("\n[API Check] Kiểm tra API reqres.in...");
        Response response = given(requestSpec).get("/users");
        isApiAlive = (response.getStatusCode() == 200);

        if (!isApiAlive) {
            throw new SkipException("API backend lỗi, SKIP test UI.");
        }
        System.out.println("[API Check] API is ALIVE.");

        loginSauceDemoUI("standard_user", "secret_sauce");

        System.out.println("[UI Action] Thêm sản phẩm vào giỏ...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_item")));

        List<WebElement> addButtons = driver.findElements(By.className("btn_inventory"));
        addButtons.get(0).click();
        addButtons.get(1).click();

        String badgeCount = driver.findElement(By.className("shopping_cart_badge")).getText();
        System.out.println("[UI Assertion] Badge cart: " + badgeCount);
        Assert.assertEquals(badgeCount, "2");

        // Vào trang giỏ hàng
        driver.findElement(By.className("shopping_cart_link")).click();

        int cartItemsCount = driver.findElements(By.className("cart_item")).size();
        System.out.println("[UI Assertion] Số item trong giỏ: " + cartItemsCount);
        Assert.assertEquals(cartItemsCount, 2);
    }

    @AfterMethod(alwaysRun = true)
    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            System.out.println("[Cleanup] Đã đóng trình duyệt.");
        }
    }
}
