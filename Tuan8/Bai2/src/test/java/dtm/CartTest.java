package dtm;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;

public class CartTest {

    WebDriver driver;

    private static final String BASE_URL = "https://www.saucedemo.com";
    private static final String VALID_USER = "standard_user";
    private static final String VALID_PASSWORD = "secret_sauce";

    // Locators
    private static final By USERNAME_FIELD = By.id("user-name");
    private static final By PASSWORD_FIELD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ADD_TO_CART_FIRST = By.cssSelector(".inventory_item:first-child button");
    private static final By CART_BADGE = By.className("shopping_cart_badge");
    private static final By CART_ICON = By.className("shopping_cart_link");
    private static final By CART_ITEMS = By.className("cart_item");
    private static final By REMOVE_BUTTON = By.cssSelector(".cart_item button");
    private static final By CONTINUE_SHOPPING = By.id("continue-shopping");

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(BASE_URL);

        // Đăng nhập trước mỗi test
        driver.findElement(USERNAME_FIELD).sendKeys(VALID_USER);
        driver.findElement(PASSWORD_FIELD).sendKeys(VALID_PASSWORD);
        driver.findElement(LOGIN_BUTTON).click();
    }

    @Test(groups = { "smoke",
            "regression" }, description = "TC-C-01: Them san pham vao gio hang - kiem tra badge so luong")
    public void testAddItemToCart() {
        System.out.println("[CartTest] TC-C-01 - testAddItemToCart: STARTING");
        Assert.assertTrue(driver.getCurrentUrl().contains("/inventory.html"),
                "Phai o trang inventory sau khi dang nhap!");

        driver.findElement(ADD_TO_CART_FIRST).click();

        WebElement badge = driver.findElement(CART_BADGE);
        Assert.assertTrue(badge.isDisplayed(), "Badge gio hang phai hien thi sau khi them san pham!");
        Assert.assertEquals(badge.getText(), "1", "Badge phai hien thi so luong la 1!");

        System.out.println("[CartTest] TC-C-01 - testAddItemToCart: PASSED -> Badge = " + badge.getText());
    }

    @Test(groups = { "regression" }, description = "TC-C-02: Xem gio hang - kiem tra co 1 san pham sau khi them")
    public void testViewCartWithItem() {
        System.out.println("[CartTest] TC-C-02 - testViewCartWithItem: STARTING");

        driver.findElement(ADD_TO_CART_FIRST).click();
        driver.findElement(CART_ICON).click();

        Assert.assertTrue(driver.getCurrentUrl().contains("/cart.html"),
                "URL phai chua '/cart.html' khi vao trang gio hang!");

        List<WebElement> cartItems = driver.findElements(CART_ITEMS);
        Assert.assertEquals(cartItems.size(), 1,
                "Gio hang phai co dung 1 san pham sau khi them 1 san pham!");

        System.out.println("[CartTest] TC-C-02 - testViewCartWithItem: PASSED -> Cart items = " + cartItems.size());
    }


    @Test(groups = {
            "regression" }, description = "TC-C-03: Xoa san pham khoi gio hang - gio hang phai trong va badge bien mat")
    public void testRemoveItemFromCart() {
        System.out.println("[CartTest] TC-C-03 - testRemoveItemFromCart: STARTING");

        driver.findElement(ADD_TO_CART_FIRST).click();
        driver.findElement(CART_ICON).click();

        // Xoá item
        driver.findElement(REMOVE_BUTTON).click();

        List<WebElement> cartItems = driver.findElements(CART_ITEMS);
        Assert.assertEquals(cartItems.size(), 0, "Gio hang phai trong sau khi xoa san pham!");

        List<WebElement> badges = driver.findElements(CART_BADGE);
        Assert.assertEquals(badges.size(), 0, "Badge gio hang phai bien mat khi gio hang trong!");

        System.out.println("[CartTest] TC-C-03 - testRemoveItemFromCart: PASSED -> Cart empty after removal.");
    }

    @Test(groups = { "regression" }, description = "TC-C-04: Nut Continue Shopping phai quay lai trang inventory")
    public void testContinueShopping() {
        System.out.println("[CartTest] TC-C-04 - testContinueShopping: STARTING");

        driver.findElement(CART_ICON).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("/cart.html"),
                "URL phai chua '/cart.html'!");

        driver.findElement(CONTINUE_SHOPPING).click();

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/inventory.html"),
                "Sau khi bam 'Continue Shopping', phai quay lai '/inventory.html'. URL: " + currentUrl);

        System.out.println("[CartTest] TC-C-04 - testContinueShopping: PASSED -> URL = " + currentUrl);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
