package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class Search {

    public static void main(String[] args) {

        WebDriver driver = Main.startBrowser();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            // ACCEPT COOKIES
            List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
            for (WebElement iframe : iframes) {
                if (iframe.getAttribute("src") != null &&
                        iframe.getAttribute("src").contains("consent")) {
                    driver.switchTo().frame(iframe);
                    break;
                }
            }

            WebElement acceptCookies = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.id("onetrust-accept-btn-handler")
                    )
            );

            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", acceptCookies);

            driver.switchTo().defaultContent();
            System.out.println("✅ Cookies accepted");

            // CLICK SEARCH ICON
            WebElement searchButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.cssSelector(
                                    ".bg-color-white-alpha-10.border-solid.border-color-white-alpha-20"
                            )
                    )
            );
            searchButton.click();
            System.out.println("✅ Search icon clicked");

            // CLICK SEARCH INPUT & TYPE
            WebElement searchInput = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.id("search-input")
                    )
            );
            searchInput.click();
            searchInput.sendKeys("ifs");
            System.out.println("✅ Typed 'ifs' into search");

            // WAIT FOR PREDICTIVE SEARCH RESULTS
            List<WebElement> suggestions = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(
                            By.cssSelector("li[role='option']")
                    )
            );

            if (suggestions.isEmpty()) {
                throw new RuntimeException("❌ No predictive search results displayed");
            }

            System.out.println("🔎 Predictive search results:");
            for (int i = 0; i < suggestions.size(); i++) {
                System.out.println(" - " + suggestions.get(i).getText());
            }

            System.out.println("✅ Predictive search validation successful");

            // CLICK FIRST PREDICTIVE SEARCH ITEM
            WebElement firstSuggestion = suggestions.get(0);
            firstSuggestion.click();
            System.out.println("✅ Clicked first predictive search result");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
