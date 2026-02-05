package tests;

import base.BaseTest;
import pages.Footer;
import org.testng.annotations.Test;

public class FooterTest extends BaseTest {

    @Test
    public void validateFooterLinks() throws InterruptedException {

        Footer footer = new Footer(driver);
        footer.validateFooterIndustryLinks();
    }
}