package tests;

import base.BaseTest;
import webpages.Sitemap;
import org.testng.annotations.Test;

public class SitemapTest extends BaseTest {

    @Test
    public void validateSitemap() {
        Sitemap sitemap = new Sitemap(driver);
        sitemap.validateSitemapHreflang();
    }
}
