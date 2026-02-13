package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import webpages.Homepage;

public class HomepageTest extends BaseTest {

    @Test
    public void homepageComponentValidation() {

        // Open homepage directly
        driver.get("https://www.ifs.com/en");

        Homepage home = new Homepage(driver);

        // ================= HERO =================
        home.validateHeroBanner();
        home.navigateBackToHome();

        // ================= INTRO =================
        home.validateIntroText();

        // ================= PROMO 1 =================
        home.validateFirstPromo();
        home.navigateBackToHome();

        // ================= ACCORDION =================
        home.validateAccordionComponent();
        home.navigateBackToHome();

        // ================= MEDIA CAROUSEL =================
        home.validateMediaCarousel();

        // ================= FACT SHEET =================
        home.validateFactSheet();

        // ================= QUOTE =================
        home.validateQuoteComponent();

        // ================= LOGO CAROUSEL =================
        home.validateLogoCarousel();

        // ================= PROMO 2 =================
        home.validateSecondPromo();

        // ================= NEWS & UPDATES =================
        home.validateNewsAndUpdates();

        // ================= FINAL CLEAN STATE =================
        home.navigateBackToHome();

        // ================= get started form  =================
        home.validateGetStartedForm();

    }
}
