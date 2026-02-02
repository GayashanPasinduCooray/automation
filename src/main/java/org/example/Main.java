package org.example;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main {

    public static WebDriver driver;

    public static WebDriver startBrowser() {

        if (driver == null) {
            System.setProperty(
                    "webdriver.edge.driver",
                    "C:\\Users\\cogalk\\OneDrive - IFS\\Desktop\\web driver\\msedgedriver.exe"
            );

            driver = new EdgeDriver();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            driver.manage().window().maximize();
            driver.get("https://www.ifs.com");

            System.out.println("Page Title: " + driver.getTitle());

            acceptCookies();   // ✅ AUTO ACCEPT COOKIES
        }

        return driver;
    }

    // ================= COOKIE HANDLER =================
    private static void acceptCookies() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Switch to cookie iframe if present
            List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
            for (WebElement iframe : iframes) {
                if (iframe.getAttribute("src") != null &&
                        iframe.getAttribute("src").contains("consent")) {
                    driver.switchTo().frame(iframe);
                    break;
                }
            }

            WebElement acceptBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.id("onetrust-accept-btn-handler")
                    )
            );

            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", acceptBtn);

            driver.switchTo().defaultContent();

            System.out.println("✅ Cookies accepted");

        } catch (TimeoutException e) {
            System.out.println("ℹ️ Cookie banner not displayed");
            driver.switchTo().defaultContent();
        } catch (Exception e) {
            System.out.println("⚠️ Cookie handling skipped");
            driver.switchTo().defaultContent();
        }
    }

    // ================= CLOSE BROWSER =================
    public static void closeBrowser() {
        if (driver != null) {
            driver.quit();
            driver = null; // reset session
            System.out.println("🧹 Browser closed");
        }
    }

    public static void main(String[] args) {
        startBrowser();
    }
}
