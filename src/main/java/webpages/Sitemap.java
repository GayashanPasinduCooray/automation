package webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ValidationResultTracker;

import java.time.Duration;

public class Sitemap {

    private WebDriver driver;
    private WebDriverWait wait;

    public Sitemap(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // VALIDATE SITEMAP HREFLANG

    public void validateSitemapHreflang() {

        long start = System.currentTimeMillis();

        try {
            String sitemapUrl = "https://www.ifs.com/sitemap.xml";
            driver.get(sitemapUrl);

            wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.tagName("urlset"))
            );

            System.out.println("🌐 Sitemap opened: " + sitemapUrl);

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
            System.out.println("nl = " + nl);
            System.out.println("fr = " + fr);
            System.out.println("de = " + de);
            System.out.println("it = " + it);
            System.out.println("pl = " + pl);
            System.out.println("pt = " + pt);
            System.out.println("es = " + es);
            System.out.println("tr = " + tr);
            System.out.println("ja = " + ja);

            if (nl && fr && de && it && pl && pt && es && tr && ja) {
                System.out.println("✅ Sitemap working as expected");
            } else {
                System.out.println("❌ Sitemap missing some hreflang entries");
            }

            // ✅ MARK PASS
            ValidationResultTracker.recordPass("Sitemap");


        } catch (Exception e) {

            // ❌ MARK FAIL
            ValidationResultTracker.recordFail("Sitemap");

            // Original message
            System.out.println("❌ Sitemap validation failed");
            e.printStackTrace();

            // re-throw so TestNG will know the test failed
            throw e;

        } finally {

            // ⏱ ALWAYS RECORD EXECUTION TIME
            ValidationResultTracker.addExecutionTime(
                    "Sitemap",
                    System.currentTimeMillis() - start
            );
        }
    }
}
