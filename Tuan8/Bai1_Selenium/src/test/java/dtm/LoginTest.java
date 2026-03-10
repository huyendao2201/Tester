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

    // -------------------------------------------------------
    // Hằng số dùng chung
    // -------------------------------------------------------
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

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(BASE_URL);
    }

    // -------------------------------------------------------
    // Test case 1: Đăng nhập thành công
    // Điều kiện: user/pass hợp lệ → phải chuyển sang /inventory.html
    // -------------------------------------------------------
    @Test(description = "Dang nhap thanh cong voi tai khoan hop le")
    public void testLoginSuccess() {
        driver.findElement(USERNAME_FIELD).sendKeys(VALID_USER);
        driver.findElement(PASSWORD_FIELD).sendKeys(VALID_PASSWORD);
        driver.findElement(LOGIN_BUTTON).click();

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(
                currentUrl.contains("/inventory.html"),
                "Sau khi dang nhap thanh cong, URL phai chua '/inventory.html'. URL hien tai: " + currentUrl);
    }

    // -------------------------------------------------------
    // Test case 2: Đăng nhập sai mật khẩu
    // Điều kiện: user đúng, pass sai → thông báo lỗi phải hiển thị
    // -------------------------------------------------------
    @Test(description = "Dang nhap sai mat khau - kiem tra thong bao loi xuat hien")
    public void testLoginWrongPassword() {
        driver.findElement(USERNAME_FIELD).sendKeys(VALID_USER);
        driver.findElement(PASSWORD_FIELD).sendKeys(WRONG_PASSWORD);
        driver.findElement(LOGIN_BUTTON).click();

        WebElement errorMsg = driver.findElement(ERROR_MESSAGE);
        Assert.assertTrue(
                errorMsg.isDisplayed(),
                "Thong bao loi phai hien thi khi dang nhap sai mat khau!");
        Assert.assertFalse(
                errorMsg.getText().isEmpty(),
                "Noi dung thong bao loi khong duoc rong!");
    }

    // -------------------------------------------------------
    // Test case 3: Bỏ trống username
    // Điều kiện: không nhập username → thông báo 'Username is required'
    // -------------------------------------------------------
    @Test(description = "Bo trong username - kiem tra thong bao 'Username is required'")
    public void testLoginEmptyUsername() {
        // Để trống username, chỉ nhập password
        driver.findElement(PASSWORD_FIELD).sendKeys(VALID_PASSWORD);
        driver.findElement(LOGIN_BUTTON).click();

        WebElement errorMsg = driver.findElement(ERROR_MESSAGE);
        Assert.assertTrue(
                errorMsg.isDisplayed(),
                "Thong bao loi phai hien thi khi de trong username!");
        Assert.assertTrue(
                errorMsg.getText().contains("Username is required"),
                "Thong bao loi phai chua 'Username is required'. Noi dung hien tai: " + errorMsg.getText());
    }

    // -------------------------------------------------------
    // Test case 4: Bỏ trống password
    // Điều kiện: nhập username, không nhập password → thông báo 'Password is
    // required'
    // -------------------------------------------------------
    @Test(description = "Bo trong password - kiem tra thong bao 'Password is required'")
    public void testLoginEmptyPassword() {
        driver.findElement(USERNAME_FIELD).sendKeys(VALID_USER);
        // Để trống password
        driver.findElement(LOGIN_BUTTON).click();

        WebElement errorMsg = driver.findElement(ERROR_MESSAGE);
        Assert.assertTrue(
                errorMsg.isDisplayed(),
                "Thong bao loi phai hien thi khi de trong password!");
        Assert.assertTrue(
                errorMsg.getText().contains("Password is required"),
                "Thong bao loi phai chua 'Password is required'. Noi dung hien tai: " + errorMsg.getText());
    }

    // -------------------------------------------------------
    // Test case 5: Tài khoản bị khoá (locked_out_user)
    // Điều kiện: dùng locked_out_user → thông báo 'Sorry, this user has been locked
    // out'
    // -------------------------------------------------------
    @Test(description = "Dang nhap voi tai khoan bi khoa - kiem tra thong bao locked out")
    public void testLoginLockedUser() {
        driver.findElement(USERNAME_FIELD).sendKeys(LOCKED_USER);
        driver.findElement(PASSWORD_FIELD).sendKeys(VALID_PASSWORD);
        driver.findElement(LOGIN_BUTTON).click();

        WebElement errorMsg = driver.findElement(ERROR_MESSAGE);
        Assert.assertTrue(
                errorMsg.isDisplayed(),
                "Thong bao loi phai hien thi voi tai khoan bi khoa!");
        Assert.assertTrue(
                errorMsg.getText().contains("Sorry, this user has been locked out"),
                "Thong bao loi phai chua 'Sorry, this user has been locked out'. Noi dung hien tai: "
                        + errorMsg.getText());
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
