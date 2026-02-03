package webpages;

import org.example.Main;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Cookies {

    public static void main(String[] args) {

        // TEST 1: ACCEPT ALL
        runTest("TEST 1: Accepted all cookies",
                By.id("onetrust-accept-btn-handler"));

        // TEST 2: REJECT ALL
        runTest("TEST 2: Rejected all cookies",
                By.id("onetrust-reject-all-handler"));

        // TEST 3: CUSTOM SETTINGS
        WebDriver driver = Main.startBrowser();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            switchToCookieIframe(driver);

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("onetrust-pc-btn-handler"))).click();
            System.out.println("⚙️ Opened cookie settings");

            toggle(wait, "ot-group-id-C0002", "Performance Cookies");
            toggle(wait, "ot-group-id-C0003", "Functional Cookies");
            toggle(wait, "ot-group-id-C0004", "Targeting Cookies");

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button.save-preference-btn-handler.onetrust-close-btn-handler")
            )).click();

            System.out.println("✅ TEST 3: Custom cookie preferences saved");

        } catch (Exception e) {
            System.out.println("❌ TEST 3 FAILED");
            e.printStackTrace();
        } finally {
            Main.closeBrowser();
        }
    }

    // REUSABLE TEST
    private static void runTest(String successMsg, By button) {
        WebDriver driver = Main.startBrowser();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            switchToCookieIframe(driver);
            wait.until(ExpectedConditions.elementToBeClickable(button)).click();
            System.out.println("✅ " + successMsg);
        } catch (Exception e) {
            System.out.println("❌ " + successMsg + " FAILED");
            e.printStackTrace();
        } finally {
            Main.closeBrowser();
        }
    }

    //  HELPERS
    private static void switchToCookieIframe(WebDriver driver) {
        for (WebElement iframe : driver.findElements(By.tagName("iframe"))) {
            String src = iframe.getAttribute("src");
            if (src != null && src.contains("consent")) {
                driver.switchTo().frame(iframe);
                break;
            }
        }
    }

    private static void toggle(WebDriverWait wait, String forAttr, String name) {
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("label.ot-switch[for='" + forAttr + "']"))).click();
        System.out.println("🔁 Toggled " + name);
    }
}
