package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import webpages.BookaDemo;

public class BookaDemoTest extends BaseTest {

    @Test
    public void bookDemoFormTest() {
        BookaDemo demoPage = new BookaDemo(driver);

        System.out.println("Page Title: " + driver.getTitle());

        demoPage.acceptCookies();
        demoPage.clickBookDemoLink();
        demoPage.fillDemoForm();
        demoPage.submitForm();

        // Validate Thank You message
        boolean isThankYouVisible = demoPage.isThankYouMessageDisplayed();
        Assert.assertTrue(isThankYouVisible, "❌ Thank You message NOT displayed");
        System.out.println("🎉 Thank You message displayed successfully!");
    }
}
