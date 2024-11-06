import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class TestContactAppAndroid {
    AppiumDriver driver;
    private static String convertMonth(String month) {
        switch (month) {
            case "Jan":
                return "January";
            case "Feb":
                return "February";
            case "Mar":
                return "March";
            case "Apr":
                return "April";
            case "May":
                return "May";
            case "Jun":
                return "June";
            case "Jul":
                return "July";
            case "Aug":
                return "August";
            case "Sep":
                return "September";
            case "Oct":
                return "October";
            case "Nov":
                return "November";
            case "Dec":
                return "December";
            default: throw new IllegalArgumentException("Invalid month abbreviation: " + month);
        }
    }

    @BeforeClass
    public void setup() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setUdid("emulator-5554")
                .setNewCommandTimeout(Duration.ofSeconds(30))
                .setAppPackage("com.google.android.contacts")
                .setAppActivity("com.android.contacts.activities.PeopleActivity");
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @Test
    public void test() {
        int randomNumb = (int) (Math.random()*100000);
        String contactFirstName = "Tung" + randomNumb;
        String contactLastName = "Huynh" + randomNumb;
        String displayName = contactFirstName + " " + contactLastName;
        String contactPhoneNumber = "123456789";
        String contactPhoneNumberDisplay = "1 (234) 567-89";

        //==================== Xpath locators =======================
        //Create contact
        String createContactButton_xpath = "//*[@content-desc=\"Create contact\"]";
        String firstNameField_xpath = "//*[@text=\"First name\"]";
        String lastNameField_xpath = "//*[@text=\"Last name\"]";
        String phoneNumberField_xpath = "//*[@text=\"Phone\"]";
        String birthDateDropDown_xpath = "//*[@content-desc=\"Show date picker\"]";
        String DOB_xpath = "//*[@resource-id=\"android:id/numberpicker_input\"]";
        String searchButton_xpath = "//*[@resource-id=\"com.google.android.contacts:id/open_search_bar\"]";
        //Search contact
        String searchField_xpath = "//*[@resource-id=\"com.google.android.contacts:id/open_search_view_edit_text\"]";
        String firstContactFound_xpath = "//*[@resource-id=\"com.google.android.contacts:id/search_result_list\"]/android.view.ViewGroup[1]";
        //Display contact
        String contactDisplayName_xpath = "//*[@resource-id=\"com.google.android.contacts:id/large_title\"]";
        String contactDisplayPhone_xpath = "(//*[@resource-id=\"com.google.android.contacts:id/header\"])[1]";
        String contactDisplayDOB_xpath = "(//*[@resource-id=\"com.google.android.contacts:id/header\"])[2]";
        String moreOptions_xpath = "//*[@content-desc=\"More options\"]";
        String deleteButton_xpath = "//*[@resource-id=\"com.google.android.contacts:id/title\" and @text=\"Delete\"]";
        String deleteConfirm_xpath = "//*[@resource-id=\"android:id/button1\"]";

        driver.findElement(AppiumBy.xpath(createContactButton_xpath)).click();
        driver.findElement(AppiumBy.xpath(firstNameField_xpath)).sendKeys(contactFirstName);
        driver.findElement(AppiumBy.xpath(lastNameField_xpath)).sendKeys(contactLastName);
        WebElement contactPhoneNumberLocator = driver.findElement(AppiumBy.xpath(phoneNumberField_xpath));
        contactPhoneNumberLocator.sendKeys(contactPhoneNumber);


        //Scroll to bottom
        final var finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        var start = new Point(1000, 1500);
        var end = new Point (1000, 500);
        var swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0),
                PointerInput.Origin.viewport(), start.getX(), start.getY()));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(1000),
                PointerInput.Origin.viewport(), end.getX(), end.getY()));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(swipe));

        //Add birthday date
        String dateLabel = driver.findElement(AppiumBy.xpath("//*[@content-desc=\"Birthday Special date\"]")).getText();
        Assert.assertEquals(dateLabel, "Birthday");
        driver.findElement(AppiumBy.xpath(birthDateDropDown_xpath)).click();
        List<WebElement> DOB = driver.findElements(AppiumBy.xpath(DOB_xpath));
        String birthMonth = DOB.get(0).getText();
        String birthDate = DOB.get(1).getText();
        String birthYear = DOB.get(2).getText();
        birthMonth = convertMonth(birthMonth);
        if ( birthDate.charAt(0) == '0' ) birthDate = birthDate.substring(1);
        String dobDisplayExpected = birthMonth + " " + birthDate + ", " + birthYear;
        //Save the contact and return to contact screen
        driver.findElement(AppiumBy.xpath("//*[@resource-id=\"android:id/button1\"]")).click();
        driver.findElement(AppiumBy.xpath("//*[@resource-id=\"com.google.android.contacts:id/toolbar_button\"]")).click();
        driver.findElement(AppiumBy.xpath("//*[@content-desc=\"Close Popup Window\"]")).click();
        driver.findElement(AppiumBy.xpath("//*[@content-desc=\"Navigate up\"]")).click();

        //Verify the contact is created and its fields are saved successfully
        driver.findElement(AppiumBy.xpath(searchButton_xpath)).click();
        driver.findElement(AppiumBy.xpath(searchField_xpath)).sendKeys(displayName);
        WebElement firstContact = driver.findElement(AppiumBy.xpath(firstContactFound_xpath));
        Assert.assertTrue(firstContact.isDisplayed());
        firstContact.click();
        String contactDisplayName = driver.findElement(AppiumBy.xpath(contactDisplayName_xpath)).getText();
        Assert.assertEquals(contactDisplayName, displayName);
        String contactDisplayPhone = driver.findElement(AppiumBy.xpath(contactDisplayPhone_xpath)).getText();
        Assert.assertEquals(contactDisplayPhone, contactPhoneNumberDisplay);
        String dobDisplay = driver.findElement(AppiumBy.xpath(contactDisplayDOB_xpath)).getText();
        Assert.assertEquals(dobDisplay, dobDisplayExpected);

        //Delete contact
        driver.findElement(AppiumBy.xpath(moreOptions_xpath)).click();
        driver.findElement(AppiumBy.xpath(deleteButton_xpath)).click();
        driver.findElement(AppiumBy.xpath(deleteConfirm_xpath)).click();

        //Verify contact is deleted successfully
        driver.findElement(AppiumBy.xpath("//*[@content-desc=\"Back\"]")).click();
        driver.findElement(AppiumBy.xpath(searchButton_xpath)).click();
        driver.findElement(AppiumBy.xpath(searchField_xpath)).sendKeys(displayName);
        List <WebElement> deletedContact = driver.findElements(AppiumBy.xpath(firstContactFound_xpath));
        Assert.assertTrue(deletedContact.isEmpty());
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
