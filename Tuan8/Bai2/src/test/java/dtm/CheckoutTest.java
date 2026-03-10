package dtm;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;

public class CheckoutTest {

        WebDriver driver;

        private static final String BASE_URL = "https://www.saucedemo.com";
        private static final String VALID_USER = "standard_user";
        private static final String VALID_PASSWORD = "secret_sauce";

        // Locators - Login
        private static final By USERNAME_FIELD = By.id("user-name");
        private static final By PASSWORD_FIELD = By.id("password");
        private static final By LOGIN_BUTTON = By.id("login-button");

        // Locators - Inventory
        private static final By ADD_TO_CART_FIRST = By.cssSelector(".inventory_item:first-child button");
        private static final By CART_ICON = By.className("shopping_cart_link");

        // Locators - Cart
        private static final By CHECKOUT_BUTTON = By.id("checkout");

        // Locators - Checkout Step 1
        private static final By FIRST_NAME_FIELD = By.id("first-name");
        private static final By LAST_NAME_FIELD = By.id("last-name");
        private static final By ZIP_CODE_FIELD = By.id("postal-code");
        private static final By CONTINUE_BUTTON = By.id("continue");
        private static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");

        // Locators - Checkout Step 2
        private static final By FINISH_BUTTON = By.id("finish");
        private static final By SUMMARY_TOTAL = By.className("summary_total_label");

        // Locators - Checkout Complete
        private static final By COMPLETE_HEADER = By.className("complete-header");
        private static final By BACK_HOME_BUTTON = By.id("back-to-products");

        @BeforeMethod(alwaysRun = true)
        public void setUp() {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                driver.manage().window().maximize();
                driver.get(BASE_URL);

                // Đăng nhập, thêm 1 sản phẩm, vào giỏ hàng
                driver.findElement(USERNAME_FIELD).sendKeys(VALID_USER);
                driver.findElement(PASSWORD_FIELD).sendKeys(VALID_PASSWORD);
                driver.findElement(LOGIN_BUTTON).click();

                // Chờ trang inventory load xong sau khi đăng nhập
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.urlContains("/inventory.html"));

                driver.findElement(ADD_TO_CART_FIRST).click();

                // Chờ cart badge xuất hiện
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge")));
                driver.findElement(CART_ICON).click();

                // Chờ trang cart load xong
                wait.until(ExpectedConditions.urlContains("/cart.html"));
        }

        @Test(groups = { "smoke",
                        "regression" }, description = "TC-CH-01: Mo trang checkout - URL phai chua '/checkout-step-one.html'")
        public void testCheckoutPageLoads() {
                System.out.println("[CheckoutTest] TC-CH-01 - testCheckoutPageLoads: STARTING");

                driver.findElement(CHECKOUT_BUTTON).click();

                String currentUrl = driver.getCurrentUrl();
                Assert.assertTrue(currentUrl.contains("/checkout-step-one.html"),
                                "URL phai chua '/checkout-step-one.html'. URL hien tai: " + currentUrl);

                WebElement firstNameField = driver.findElement(FIRST_NAME_FIELD);
                Assert.assertTrue(firstNameField.isDisplayed(),
                                "Truong 'First Name' phai hien thi tren trang checkout!");

                System.out.println("[CheckoutTest] TC-CH-01 - testCheckoutPageLoads: PASSED -> URL = " + currentUrl);
        }

        @Test(groups = {
                        "regression" }, description = "TC-CH-02: Bo trong thong tin checkout - kiem tra thong bao 'First Name is required'")
        public void testCheckoutEmptyInfo() {
                System.out.println("[CheckoutTest] TC-CH-02 - testCheckoutEmptyInfo: STARTING");

                driver.findElement(CHECKOUT_BUTTON).click();
                // Không nhập gì, bấm Continue
                driver.findElement(CONTINUE_BUTTON).click();

                WebElement errorMsg = driver.findElement(ERROR_MESSAGE);
                Assert.assertTrue(errorMsg.isDisplayed(),
                                "Thong bao loi phai hien thi khi de trong thong tin checkout!");
                Assert.assertTrue(errorMsg.getText().contains("First Name is required"),
                                "Thong bao loi phai chua 'First Name is required'. Noi dung: " + errorMsg.getText());

                System.out.println("[CheckoutTest] TC-CH-02 - testCheckoutEmptyInfo: PASSED -> Error: "
                                + errorMsg.getText());
        }

        @Test(groups = {
                        "regression" }, description = "TC-CH-03: Hoan tat checkout thanh cong - hien thi 'Thank you for your order'")
        public void testCheckoutComplete() {
                System.out.println("[CheckoutTest] TC-CH-03 - testCheckoutComplete: STARTING");

                driver.findElement(CHECKOUT_BUTTON).click();

                // Chờ form checkout step 1 load xong
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.urlContains("/checkout-step-one.html"));
                wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_NAME_FIELD));

                // Điền thông tin giao hàng
                driver.findElement(FIRST_NAME_FIELD).sendKeys("Nguyen");
                driver.findElement(LAST_NAME_FIELD).sendKeys("Van A");
                driver.findElement(ZIP_CODE_FIELD).sendKeys("70000");

                try {
                        Thread.sleep(1000);
                } catch (Exception e) {
                }
                driver.findElement(CONTINUE_BUTTON).click();

                // Chờ trang chuyển sang step 2
                wait.until(ExpectedConditions.urlContains("/checkout-step-two.html"));

                Assert.assertTrue(driver.getCurrentUrl().contains("/checkout-step-two.html"),
                                "Phai chuyen sang '/checkout-step-two.html' sau khi dien thong tin!");

                // Kiểm tra hiển thị tổng tiền
                WebElement summaryTotal = driver.findElement(SUMMARY_TOTAL);
                Assert.assertTrue(summaryTotal.isDisplayed(), "Tong tien phai hien thi tren trang overview!");

                // Hoàn tất đơn hàng
                wait.until(ExpectedConditions.elementToBeClickable(FINISH_BUTTON));
                try {
                        Thread.sleep(1000);
                } catch (Exception e) {
                }
                driver.findElement(FINISH_BUTTON).click();

                // Chờ trang chuyển sang complete
                wait.until(ExpectedConditions.urlContains("/checkout-complete.html"));

                String currentUrl = driver.getCurrentUrl();
                Assert.assertTrue(currentUrl.contains("/checkout-complete.html"),
                                "URL phai chua '/checkout-complete.html'. URL hien tai: " + currentUrl);

                WebElement completeHeader = driver.findElement(COMPLETE_HEADER);
                Assert.assertTrue(completeHeader.getText().contains("Thank you for your order"),
                                "Header phai chua 'Thank you for your order'. Noi dung: " + completeHeader.getText());

                System.out.println(
                                "[CheckoutTest] TC-CH-03 - testCheckoutComplete: PASSED -> Header: "
                                                + completeHeader.getText());
        }

        @Test(groups = {
                        "regression" }, description = "TC-CH-04: Nut 'Back Home' sau checkout phai quay lai trang inventory")
        public void testBackHomeAfterCheckout() {
                System.out.println("[CheckoutTest] TC-CH-04 - testBackHomeAfterCheckout: STARTING");

                driver.findElement(CHECKOUT_BUTTON).click();

                // Chờ form checkout step 1 load xong
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.urlContains("/checkout-step-one.html"));
                wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_NAME_FIELD));

                driver.findElement(FIRST_NAME_FIELD).sendKeys("Tran");
                driver.findElement(LAST_NAME_FIELD).sendKeys("Thi B");
                driver.findElement(ZIP_CODE_FIELD).sendKeys("10000");

                try {
                        Thread.sleep(1000);
                } catch (Exception e) {
                }
                driver.findElement(CONTINUE_BUTTON).click();

                // Chờ step 2 load xong
                wait.until(ExpectedConditions.urlContains("/checkout-step-two.html"));

                wait.until(ExpectedConditions.elementToBeClickable(FINISH_BUTTON));
                try {
                        Thread.sleep(1000);
                } catch (Exception e) {
                }
                driver.findElement(FINISH_BUTTON).click();

                // Chờ complete page
                wait.until(ExpectedConditions.urlContains("/checkout-complete.html"));

                Assert.assertTrue(driver.getCurrentUrl().contains("/checkout-complete.html"),
                                "Phai o trang checkout complete truoc khi bam Back Home!");

                wait.until(ExpectedConditions.elementToBeClickable(BACK_HOME_BUTTON));
                try {
                        Thread.sleep(1000);
                } catch (Exception e) {
                }
                driver.findElement(BACK_HOME_BUTTON).click();

                // Chờ quay lại inventory
                wait.until(ExpectedConditions.urlContains("/inventory.html"));

                String currentUrl = driver.getCurrentUrl();
                Assert.assertTrue(currentUrl.contains("/inventory.html"),
                                "Sau khi bam 'Back Home', phai quay lai '/inventory.html'. URL: " + currentUrl);

                System.out.println(
                                "[CheckoutTest] TC-CH-04 - testBackHomeAfterCheckout: PASSED -> URL = " + currentUrl);
        }

        @AfterMethod(alwaysRun = true)
        public void tearDown() {
                if (driver != null) {
                        driver.quit();
                }
        }
}
