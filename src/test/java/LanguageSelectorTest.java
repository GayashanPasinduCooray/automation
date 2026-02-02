package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class LanguageSelectorTest extends BaseTest {

    @Test
    public void validateLanguageSelector() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        boolean allPassed = true;

        try {
            // ACCEPT COOKIES
            acceptCookies(wait);

            //  LANGUAGE TESTS
            allPassed &= selectLanguage(wait, "Nederlands", "/nl");
            allPassed &= selectLanguage(wait, "Français", "/fr");
            allPassed &= selectLanguage(wait, "Deutsch", "/de");
            allPassed &= selectLanguage(wait, "Italiano", "/it");
            allPassed &= selectLanguage(wait, "Polski", "/pl");
            allPassed &= selectLanguage(wait, "Português", "/pt");
            allPassed &= selectLanguage(wait, "Español", "/es");
            allPassed &= selectLanguage(wait, "Türkçe", "/tr");
            allPassed &= selectLanguage(wait, "日本語", "/ja");

            Assert.assertTrue(allPassed, "Language selector has issues");

        } catch (Exception e) {
            Assert.fail("Language selector test failed", e);
        }
    }

    // ACCEPT COOKIES
    private void acceptCookies(WebDriverWait wait) {
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
                    ExpectedConditions.presenceOfElementLocated(
                            By.id("onetrust-accept-btn-handler")
                    )
            );

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", acceptBtn);
            driver.switchTo().defaultContent();
            System.out.println("✅ Cookies accepted");

        } catch (Exception e) {
            System.out.println("⚠️ Cookie banner not shown");
            driver.switchTo().defaultContent();
        }
    }

    // LANGUAGE SELECTION
    private boolean selectLanguage(WebDriverWait wait, String language, String expectedPath) {
        try {
            openLanguageMenu(wait);

            WebElement langOption = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.cssSelector("a.group[aria-label='" + language + "']")
                    )
            );

            langOption.click();

            wait.until(ExpectedConditions.urlContains(expectedPath));

            String currentUrl = driver.getCurrentUrl();
            System.out.println("🌍 " + language + " URL: " + currentUrl);

            return currentUrl.contains(expectedPath);

        } catch (Exception e) {
            System.out.println("❌ Failed to navigate to " + language);
            e.printStackTrace();
            return false;
        }
    }

    // OPEN LANGUAGE MENU
    private void openLanguageMenu(WebDriverWait wait) {
        WebElement languageButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("language-selector-button")
                )
        );

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", languageButton);
    }
}
