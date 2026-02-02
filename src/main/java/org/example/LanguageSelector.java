package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LanguageSelector {

    public static void main(String[] args) {

        WebDriver driver = Main.startBrowser();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        boolean allPassed = true;

        try {
            // ================== ACCEPT COOKIES ==================
            acceptCookies(driver, wait);

            // ================== LANGUAGE TESTS ==================
            allPassed &= selectLanguage(wait, driver, "Nederlands", "/nl");
            allPassed &= selectLanguage(wait, driver, "Français", "/fr");
            allPassed &= selectLanguage(wait, driver, "Deutsch", "/de");
            allPassed &= selectLanguage(wait, driver, "Italiano", "/it");
            allPassed &= selectLanguage(wait, driver, "Polski", "/pl");
            allPassed &= selectLanguage(wait, driver, "Português", "/pt");
            allPassed &= selectLanguage(wait, driver, "Español", "/es");
            allPassed &= selectLanguage(wait, driver, "Türkçe", "/tr");
            allPassed &= selectLanguage(wait, driver, "日本語", "/ja");

            if (allPassed) {
                System.out.println("✅ Language selector working as expected");
            } else {
                System.out.println("❌ Language selector has issues");
            }

        } catch (Exception e) {
            System.out.println("❌ Language selector test failed");
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
                    ExpectedConditions.presenceOfElementLocated(
                            By.id("onetrust-accept-btn-handler")
                    )
            );

            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", acceptBtn);

            driver.switchTo().defaultContent();
            System.out.println("✅ Cookies accepted");

        } catch (Exception e) {
            System.out.println("⚠️ Cookie banner not shown");
            driver.switchTo().defaultContent();
        }
    }

    // ================== LANGUAGE SELECTION ==================

    private static boolean selectLanguage(
            WebDriverWait wait,
            WebDriver driver,
            String language,
            String expectedPath
    ) {
        try {
            openLanguageMenu(wait, driver);

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

    // ================== OPEN LANGUAGE MENU (FIXED) ==================

    private static void openLanguageMenu(WebDriverWait wait, WebDriver driver) {
        WebElement languageButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("language-selector-button")
                )
        );

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", languageButton);
    }
}
