package base;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;


import java.time.Duration;
import java.util.List;

public class BaseTest {

    protected WebDriver driver;
    @BeforeMethod
    public void setUp() {

        System.setProperty(
                "webdriver.edge.driver",
                "C:\\Users\\cogalk\\OneDrive - IFS\\Desktop\\web driver\\msedgedriver.exe"
        );

        driver = new EdgeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        driver.get("https://www.ifs.com");
        System.out.println("Page Title: " + driver.getTitle());

        acceptCookiesIfPresent();
    }

    private void acceptCookiesIfPresent() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // Switch to OneTrust iframe
            List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
            for (WebElement iframe : iframes) {
                String src = iframe.getAttribute("src");
                if (src != null && src.contains("consent")) {
                    driver.switchTo().frame(iframe);
                    break;
                }
            }

            WebElement acceptBtn = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.id("onetrust-accept-btn-handler")
                    )
            );

            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", acceptBtn);

            driver.switchTo().defaultContent();
            System.out.println("✅ Cookies accepted");

        } catch (Exception e) {
            // Cookie banner may not appear every time
            driver.switchTo().defaultContent();
            System.out.println("ℹ️ Cookie banner not shown");
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("🧹 Browser closed");
        }
    }
}
