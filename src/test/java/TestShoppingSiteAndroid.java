import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class TestShoppingSiteAndroid {
    AppiumDriver driver;

    @BeforeClass
    public void setup() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setUdid("emulator-5554")
                .setNewCommandTimeout(Duration.ofSeconds(30))
                .withBrowserName("Chrome");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(8));
    }

    @Test
    public void test() {
        driver.get("https://magento.softwaretestingboard.com/push-it-messenger-bag.html");
        WebElement toggleNav = driver.findElement(By.xpath("//*[@class=\"action nav-toggle\"]"));
        toggleNav.click();
        WebElement womenCategory = driver.findElement(By.xpath("//*[@id=\"ui-id-4\"]"));
        womenCategory.click();
        WebElement firstWomenCategory = driver.findElement(By.xpath("(//*[@class=\"ui-menu-item all-category\"])[1]"));
        firstWomenCategory.click();
        WebElement firstItem = driver.findElement(By.xpath("(//*[@class=\"product-item-details\"])[1]"));
        driver.executeScript("arguments[0].scrollIntoView(true)", firstItem);
        String firstItemName = firstItem.findElement(By.xpath("//*[@class=\"product-item-name\"]")).getText();
        WebElement firstItem_addToCart_button = firstItem.findElement(By.xpath("//*[@data-role=\"tocart-form\"]"));
        firstItem_addToCart_button.click();



        WebElement productTitle = driver.findElement(By.cssSelector("h1.page-title"));
        String productName = productTitle.getText();
        Assert.assertEquals(productName, firstItemName);
        WebElement sizeOption = driver.findElement(By.xpath("(//*[@class=\"swatch-attribute-options clearfix\"])[1]"));
        driver.executeScript("arguments[0].scrollIntoView(true)", productTitle);

        WebElement smallestSize = driver.findElement(By.xpath("(//*[@class=\"swatch-option text\"])[1]"));
        String size = smallestSize.getText();
        smallestSize.click();
        WebElement colorOption = driver.findElement(By.xpath("(//*[@class=\"swatch-attribute-options clearfix\"])[2]"));
        WebElement firstColor = driver.findElement(By.xpath("(//*[@class=\"swatch-option color\"])[1]"));
        String color = firstColor.getAttribute("aria-label");
        firstColor.click();
        WebElement addToCartButton = driver.findElement(By.id("product-addtocart-button"));
        addToCartButton.click();

        WebElement topMessage = driver.findElement(By.xpath("//*[@class=\"message global demo\"]"));
        driver.executeScript("arguments[0].scrollIntoView(true)", topMessage);
        WebElement messageAdded = driver.findElement(By.xpath("//*[@data-ui-id=\"message-success\"]"));
        String messageSucceed = messageAdded.getText();
        Assert.assertEquals(messageSucceed, "You added " + productName + " to your shopping cart.");
        WebElement cartCounter = driver.findElement(By.xpath("//*[@class=\"counter-label\"]"));
        String actualCartCounter = cartCounter.getText();
        Assert.assertEquals("1", actualCartCounter);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
