package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import webpages.Footer;

public class FooterTest extends BaseTest {

    @Test
    public void validateFooterLinks() {

        // ================= START MESSAGE =================
        System.out.println("================+ Footer TESTING START +================" );

        Footer footer = new Footer(driver);
        footer.validateAllFooterLinks();

        //================= End MESSAGE =================
        System.out.println("================+ Footer TESTING END +================" );
    }
}
