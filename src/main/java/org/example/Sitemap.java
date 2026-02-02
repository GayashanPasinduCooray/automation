package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class Sitemap {

    public static void main(String[] args) {

        WebDriver driver = Main.startBrowser();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            // ================== ACCEPT COOKIES ==================
            acceptCookies(driver, wait);

            // ================== OPEN SITEMAP (ROOT URL) ==================
            String sitemapUrl = "https://www.ifs.com/sitemap.xml";
            driver.get(sitemapUrl);

            // Wait until XML loads
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("urlset")));

            System.out.println("🌐 Sitemap opened: " + sitemapUrl);

            // ================== VALIDATE HREFLANG ==================
            String pageSource = driver.getPageSource();

            boolean nl = pageSource.contains("hreflang=\"nl\"");
            boolean fr = pageSource.contains("hreflang=\"fr\"");
            boolean de = pageSource.contains("hreflang=\"de\"");
            boolean it = pageSource.contains("hreflang=\"it\"");
            boolean pl = pageSource.contains("hreflang=\"pl\"");
            boolean pt = pageSource.contains("hreflang=\"pt\"");
            boolean es = pageSource.contains("hreflang=\"es\"");
            boolean tr = pageSource.contains("hreflang=\"tr\"");
            boolean ja = pageSource.contains("hreflang=\"ja\"");

            System.out.println("🔍 hreflang validation:");
            System.out.println("nl=" + nl);
            System.out.println("fr=" + fr);
            System.out.println("de=" + de);
            System.out.println("it=" + it);
            System.out.println("pl=" + pl);
            System.out.println("pt=" + pt);
            System.out.println("es=" + es);
            System.out.println("tr=" + tr);
            System.out.println("ja=" + ja);

            if (nl && fr && de && it && pl && pt && es && tr && ja) {
                System.out.println("✅ Sitemap working as expected");
            } else {
                System.out.println("❌ Sitemap missing some hreflang entries");
            }

        } catch (Exception e) {
            System.out.println("❌ Sitemap validation failed");
            e.printStackTrace();
        } finally {
            Main.closeBrowser();
        }
    }

    // ================== ACCEPT COOKIES ==================

    private static void acceptCookies(WebDriver driver, WebDriverWait wait) {
        try {
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

        } catch (Exception e) {
            System.out.println("⚠️ Cookie banner not displayed");
            driver.switchTo().defaultContent();
        }
    }
}
