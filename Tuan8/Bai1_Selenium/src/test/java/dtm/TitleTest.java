package dtm;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class TitleTest {

    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com");
    }
    @Test(description = "Kiem thu tieu de trang chu")
    public void testTitle() {
        String expectedTitle = "Swag Labs";
        String actualTitle = driver.getTitle();
        Assert.assertEquals(actualTitle, expectedTitle, "Tieu de trang khong dung!");
    }
    @Test(description = "Kiem thu URL trang chu")
    public void testURL() {
        String actualUrl = driver.getCurrentUrl();
        Assert.assertTrue(actualUrl.contains("saucedemo"), "URL khong hop le!");
    }

    @Test(description = "Kiem thu nguon trang (page source)")
    public void testPageSource() {
        String pageSource = driver.getPageSource();

        // Nguồn trang không được rỗng
        Assert.assertNotNull(pageSource, "Page source khong duoc null!");
        Assert.assertFalse(pageSource.isEmpty(), "Page source khong duoc rong!");

        // Kiểm tra tên thương hiệu xuất hiện trong nguồn trang
        Assert.assertTrue(
                pageSource.contains("Swag Labs"),
                "Page source khong chua ten thuong hieu 'Swag Labs'!");

        // Kiểm tra thẻ form đăng nhập tồn tại trong nguồn trang
        Assert.assertTrue(
                pageSource.contains("login_button"),
                "Page source khong chua nut dang nhap (login_button)!");

        // Kiểm tra trường nhập username tồn tại trong nguồn trang
        Assert.assertTrue(
                pageSource.contains("user-name"),
                "Page source khong chua truong nhap 'user-name'!");

        // Kiểm tra trường nhập password tồn tại trong nguồn trang
        Assert.assertTrue(
                pageSource.contains("password"),
                "Page source khong chua truong nhap 'password'!");
    }

    @Test(description = "Kiem thu form dang nhap co hien thi hay khong")
    public void testLoginFormDisplayed() {
        // --- Kiểm tra ô nhập Username ---
        WebElement usernameField = driver.findElement(By.id("user-name"));
        Assert.assertTrue(
                usernameField.isDisplayed(),
                "Truong nhap Username khong hien thi!");
        Assert.assertTrue(
                usernameField.isEnabled(),
                "Truong nhap Username khong the nhap lieu!");

        // --- Kiểm tra ô nhập Password ---
        WebElement passwordField = driver.findElement(By.id("password"));
        Assert.assertTrue(
                passwordField.isDisplayed(),
                "Truong nhap Password khong hien thi!");
        Assert.assertTrue(
                passwordField.isEnabled(),
                "Truong nhap Password khong the nhap lieu!");

        // --- Kiểm tra nút Login ---
        WebElement loginButton = driver.findElement(By.id("login-button"));
        Assert.assertTrue(
                loginButton.isDisplayed(),
                "Nut Login khong hien thi!");
        Assert.assertTrue(
                loginButton.isEnabled(),
                "Nut Login khong the bam duoc!");

        // --- Xác nhận placeholder của các trường ---
        String usernamePlaceholder = usernameField.getAttribute("placeholder");
        Assert.assertEquals(
                usernamePlaceholder, "Username",
                "Placeholder cua truong Username khong dung!");

        String passwordPlaceholder = passwordField.getAttribute("placeholder");
        Assert.assertEquals(
                passwordPlaceholder, "Password",
                "Placeholder cua truong Password khong dung!");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
