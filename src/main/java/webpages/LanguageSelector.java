package webpages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class LanguageSelector {

    private WebDriver driver;
    private WebDriverWait wait;

    // ---------- CONSTRUCTOR ----------
    public LanguageSelector(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ---------- PUBLIC METHOD (CALLED BY TEST) ----------
    public void validateAllLanguages() {

        validateLanguage("Nederlands", "/nl");
        validateLanguage("Français", "/fr");
        validateLanguage("Deutsch", "/de");
        validateLanguage("Italiano", "/it");
        validateLanguage("Polski", "/pl");
        validateLanguage("Português", "/pt");
        validateLanguage("Español", "/es");
        validateLanguage("Türkçe", "/tr");
        validateLanguage("日本語", "/ja");
    }

    // ---------- LANGUAGE VALIDATION ----------
    private void validateLanguage(String language, String expectedPath) {

        openLanguageMenu();

        WebElement langOption = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("a.group[aria-label='" + language + "']")
                )
        );

        langOption.click();

        wait.until(ExpectedConditions.urlContains(expectedPath));

        String currentUrl = driver.getCurrentUrl();
        System.out.println("🌍 " + language + " URL: " + currentUrl);

        Assert.assertTrue(
                currentUrl.contains(expectedPath),
                "Language navigation failed for: " + language
        );
    }

    // ---------- OPEN LANGUAGE MENU ----------
    private void openLanguageMenu() {

        WebElement languageButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("language-selector-button")
                )
        );

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", languageButton);
    }

    // ---------- ACCEPT COOKIES (OPTIONAL SAFETY) ----------
    // You already handle cookies in BaseTest, so this is not required.
    // Keep only if some pages still show the banner.
    @SuppressWarnings("unused")
    private void acceptCookiesIfPresent() {
        try {
            List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
            for (WebElement iframe : iframes) {
                String src = iframe.getAttribute("src");
                if (src != null && src.contains("consent")) {
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

        } catch (Exception e) {
            driver.switchTo().defaultContent();
        }
    }
}
