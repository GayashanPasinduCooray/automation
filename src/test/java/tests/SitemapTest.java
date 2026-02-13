package tests;

import base.BaseTest;
import webpages.Sitemap;
import org.testng.annotations.Test;

public class SitemapTest extends BaseTest {

    @Test
    public void validateSitemap() {
        // ================= START MESSAGE =================
        System.out.println("================+ Sitemap TESTING START +================" );

        Sitemap sitemap = new Sitemap(driver);
        sitemap.validateSitemapHreflang();

        // ================= END MESSAGE =================
        System.out.println("================+ Sitemap TESTING END +================" );

    }
}
