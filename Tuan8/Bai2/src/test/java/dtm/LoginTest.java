package dtm;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class LoginTest {

        WebDriver driver;

        private static final String BASE_URL = "https://www.saucedemo.com";
        private static final String VALID_USER = "standard_user";
        private static final String VALID_PASSWORD = "secret_sauce";
        private static final String LOCKED_USER = "locked_out_user";
        private static final String WRONG_PASSWORD = "wrong_password_123";

        // Locators
        private static final By USERNAME_FIELD = By.id("user-name");
        private static final By PASSWORD_FIELD = By.id("password");
        private static final By LOGIN_BUTTON = By.id("login-button");
        private static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");

        @BeforeMethod(alwaysRun = true)
        public void setUp() {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                driver.manage().window().maximize();
                driver.get(BASE_URL);
        }

        @Test(groups = { "smoke", "regression" }, description = "TC-L-01: Dang nhap thanh cong voi tai khoan hop le")
        public void testLoginSuccess() {
                System.out.println("[LoginTest] TC-L-01 - testLoginSuccess: STARTING");

                driver.findElement(USERNAME_FIELD).sendKeys(VALID_USER);
                driver.findElement(PASSWORD_FIELD).sendKeys(VALID_PASSWORD);
                driver.findElement(LOGIN_BUTTON).click();

                String currentUrl = driver.getCurrentUrl();
                Assert.assertTrue(
                                currentUrl.contains("/inventory.html"),
                                "Sau khi dang nhap thanh cong, URL phai chua '/inventory.html'. URL hien tai: "
                                                + currentUrl);

                System.out.println("[LoginTest] TC-L-01 - testLoginSuccess: PASSED -> URL = " + currentUrl);
        }

        @Test(groups = { "regression" }, description = "TC-L-02: Dang nhap sai mat khau - kiem tra thong bao loi xuat hien")
        public void testLoginWrongPassword() {
                System.out.println("[LoginTest] TC-L-02 - testLoginWrongPassword: STARTING");

                driver.findElement(USERNAME_FIELD).sendKeys(VALID_USER);
                driver.findElement(PASSWORD_FIELD).sendKeys(WRONG_PASSWORD);
                driver.findElement(LOGIN_BUTTON).click();

                WebElement errorMsg = driver.findElement(ERROR_MESSAGE);
                Assert.assertTrue(errorMsg.isDisplayed(),
                                "Thong bao loi phai hien thi khi dang nhap sai mat khau!");
                Assert.assertFalse(errorMsg.getText().isEmpty(),
                                "Noi dung thong bao loi khong duoc rong!");

                System.out.println(
                                "[LoginTest] TC-L-02 - testLoginWrongPassword: PASSED -> Error: " + errorMsg.getText());
        }

        @Test(groups = {
                        "regression" }, description = "TC-L-03: Bo trong username - kiem tra thong bao 'Username is required'")
        public void testLoginEmptyUsername() {
                System.out.println("[LoginTest] TC-L-03 - testLoginEmptyUsername: STARTING");

                driver.findElement(PASSWORD_FIELD).sendKeys(VALID_PASSWORD);
                driver.findElement(LOGIN_BUTTON).click();

                WebElement errorMsg = driver.findElement(ERROR_MESSAGE);
                Assert.assertTrue(errorMsg.isDisplayed(),
                                "Thong bao loi phai hien thi khi de trong username!");
                Assert.assertTrue(errorMsg.getText().contains("Username is required"),
                                "Thong bao loi phai chua 'Username is required'. Noi dung: " + errorMsg.getText());

                System.out.println(
                                "[LoginTest] TC-L-03 - testLoginEmptyUsername: PASSED -> Error: " + errorMsg.getText());
        }

        @Test(groups = {
                        "regression" }, description = "TC-L-04: Dang nhap voi tai khoan bi khoa - kiem tra thong bao locked out")
        public void testLoginLockedUser() {
                System.out.println("[LoginTest] TC-L-04 - testLoginLockedUser: STARTING");

                driver.findElement(USERNAME_FIELD).sendKeys(LOCKED_USER);
                driver.findElement(PASSWORD_FIELD).sendKeys(VALID_PASSWORD);
                driver.findElement(LOGIN_BUTTON).click();

                WebElement errorMsg = driver.findElement(ERROR_MESSAGE);
                Assert.assertTrue(errorMsg.isDisplayed(),
                                "Thong bao loi phai hien thi voi tai khoan bi khoa!");
                Assert.assertTrue(errorMsg.getText().contains("Sorry, this user has been locked out"),
                                "Thong bao loi phai chua 'Sorry, this user has been locked out'. Noi dung: "
                                                + errorMsg.getText());

                System.out.println("[LoginTest] TC-L-04 - testLoginLockedUser: PASSED -> Error: " + errorMsg.getText());
        }

        @AfterMethod(alwaysRun = true)
        public void tearDown() {
                if (driver != null) {
                        driver.quit();
                }
        }
}
