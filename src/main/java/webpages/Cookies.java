package webpages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Cookies {

    private WebDriver driver;
    private WebDriverWait wait;

    public Cookies(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Reset cookies to override BaseTest auto-accept
        driver.manage().deleteAllCookies();
        driver.navigate().refresh();
        System.out.println("♻️ Cookies reset to override BaseTest");
    }

    // ================= ACTIONS =================

    public void acceptAll() {
        switchToCookieContext();
        click(By.id("onetrust-accept-btn-handler"));
        System.out.println("✅ Accepted all cookies");
    }

    public void rejectAll() {
        switchToCookieContext();
        click(By.id("onetrust-reject-all-handler"));
        System.out.println("✅ Rejected all cookies");
    }

    public void setCustomCookies() {
        switchToCookieContext();

        click(By.id("onetrust-pc-btn-handler"));
        System.out.println("⚙️ Opened cookie settings");

        toggle("ot-group-id-C0002", "Performance Cookies");
        toggle("ot-group-id-C0003", "Functional Cookies");
        toggle("ot-group-id-C0004", "Targeting Cookies");

        click(By.cssSelector("button.save-preference-btn-handler.onetrust-close-btn-handler"));
        System.out.println("✅ Custom cookie preferences saved");
    }

    //  HELPERS

    private void switchToCookieContext() {

        driver.switchTo().defaultContent();

        try {
            Thread.sleep(2000); // allow OneTrust to load
        } catch (InterruptedException ignored) {}

        // Try iframe first
        for (WebElement iframe : driver.findElements(By.tagName("iframe"))) {
            String src = iframe.getAttribute("src");
            if (src != null && src.contains("consent")) {
                driver.switchTo().frame(iframe);
                System.out.println("🧩 Switched to cookie iframe");
                return;
            }
        }

        // If no iframe, banner is in main DOM
        System.out.println("🧩 Cookie banner in main DOM (no iframe)");
    }

    private void toggle(String forAttr, String name) {
        WebElement toggle = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("label.ot-switch[for='" + forAttr + "']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", toggle);
        System.out.println("🔁 Toggled " + name);
    }

    private void click(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }
}
