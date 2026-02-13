package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import webpages.Cookies;

public class CookiesTest extends BaseTest {



    @Test
    public void acceptCookiesTest() {

        // ================= START MESSAGE =================
        System.out.println("================+ Cookies TESTING START +================" );

        Cookies cookies = new Cookies(driver);
        cookies.acceptAll();
    }

    @Test
    public void rejectCookiesTest() {
        Cookies cookies = new Cookies(driver);
        cookies.rejectAll();
    }

    @Test
    public void customCookiesTest() {
        Cookies cookies = new Cookies(driver);
        cookies.setCustomCookies();

        // ================= END MESSAGE =================
        System.out.println("================+ Cookies TESTING END +================" );

    }
}
