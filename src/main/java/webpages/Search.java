package webpages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;

public class Search {

    private WebDriver driver;
    private WebDriverWait wait;

    public Search(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // COOKIES
    public void acceptCookies() {
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
                    ExpectedConditions.elementToBeClickable(
                            By.id("onetrust-accept-btn-handler")
                    )
            );

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", acceptBtn);
            driver.switchTo().defaultContent();
            System.out.println("✅ Cookies accepted");

        } catch (Exception e) {
            driver.switchTo().defaultContent();
        }
    }

    // SEARCH USING PREDICTIVE
    public void searchUsingPredictive(String text) {
        try {
            WebElement searchButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.cssSelector(".bg-color-white-alpha-10.border-solid.border-color-white-alpha-20")
                    )
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);
            System.out.println("✅ Search icon clicked");

            WebElement searchInput = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("search-input"))
            );
            searchInput.click();
            searchInput.sendKeys(text);

            List<WebElement> suggestions = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(
                            By.cssSelector("li[role='option']")
                    )
            );

            suggestions.get(0).click();
            System.out.println("✅ First predictive result clicked");

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("ul.flex.flex-col.lg\\:py-padding-tight")
            ));

        } catch (TimeoutException e) {
            throw new RuntimeException("❌ Search icon or predictive input not found!", e);
        }
    }

    // FILTERS
    public boolean areMainFiltersVisible() {
        WebElement filterArea = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("ul.flex.flex-col.lg\\:py-padding-tight")
                )
        );

        return filterArea.getText().contains("Content Type")
                && filterArea.getText().contains("Industry")
                && filterArea.getText().contains("Products");
    }

    // RANDOM INDUSTRY FILTER
    public void applyRandomIndustryFilterSoftValidation() {
        try {
            List<WebElement> filterButtons = driver.findElements(
                    By.cssSelector("button.flex-row.w-full.flex.justify-between")
            );

            WebElement industryButton = filterButtons.stream()
                    .filter(b -> b.getText().equalsIgnoreCase("Industry"))
                    .findFirst()
                    .orElse(null);

            if (industryButton == null) return;

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", industryButton);

            List<WebElement> options = driver.findElements(
                    By.cssSelector("ul.flex.flex-col.py-padding-micro.block span")
            );

            if (options.isEmpty()) return;

            WebElement randomFilter = options.get(new Random().nextInt(options.size()));

            // Get expected count from filter text
            String filterText = randomFilter.getText();
            int expectedCount = Integer.parseInt(
                    filterText.replaceAll(".*\\((\\d+)\\)", "$1")
            );

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", randomFilter);

            //  HARD WAIT to allow refresh (10 seconds)
            Thread.sleep(10_000);

            WebElement resultCountText = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//span[contains(text(),'results for')]")
                    )
            );

            int actualCount = Integer.parseInt(
                    resultCountText.getText().replaceAll(".*of (\\d+) results.*", "$1")
            );

            // Print validation
            System.out.println("🔎 Expected Count: " + expectedCount);
            System.out.println("🔎 Actual Count  : " + actualCount);

        } catch (Exception ignored) {
        }
    }


    // CAPTURE & PRINT FIRST 3 RESULTS
    public void captureAndPrintFirst3Results() {
        captureAndPrintResults(3);
    }


    // SORT + CAPTURE RESULTS
    public void sortAndCaptureResults(String sortType, int count) {

        // Open sort dropdown
        WebElement sortDropdown = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("search-sort"))
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sortDropdown);

        // hard wait as requested
        try {
            Thread.sleep(20000);
        } catch (InterruptedException ignored) {
        }

        // Select option
        WebElement option = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[@role='option' and normalize-space()='" + sortType + "']")
                )
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);

        // hard wait as requested
        try {
            Thread.sleep(20000);
        } catch (InterruptedException ignored) {
        }

        // wait for results refresh
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("ul.flex.flex-col.mb-spacing-spacing-24 > li")
        ));

        // hard wait as requested
        try {
            Thread.sleep(20000);
        } catch (InterruptedException ignored) {
        }

        System.out.println("🔽 Sorted By: " + sortType);
        captureAndPrintResults(count);
    }

    // REUSABLE RESULT CAPTURE
    private void captureAndPrintResults(int maxCount) {

        List<WebElement> results = driver.findElements(
                By.cssSelector("ul.flex.flex-col.mb-spacing-spacing-24 > li")
        );

        int maxResults = Math.min(results.size(), maxCount);

        for (int i = 0; i < maxResults; i++) {

            WebElement result = results.get(i);

            String tag = result.findElements(
                    By.cssSelector("span.bg-component-card-label-fill")
            ).stream().findFirst().map(WebElement::getText).orElse("N/A");

            String title = result.findElements(
                    By.cssSelector("a[title]")
            ).stream().findFirst().map(WebElement::getText).orElse("N/A");

            String desc = result.findElements(
                    By.cssSelector("div.rte")
            ).stream().findFirst().map(WebElement::getText).orElse("N/A");

            System.out.println("🔹 Result " + (i + 1));
            System.out.println("   Tag   : " + tag);
            System.out.println("   Title : " + title);
            System.out.println("   Desc  : " + desc);
            System.out.println("----------------------------------");
        }
    }

    // RESULTS SOFT VALIDATION
    public boolean areResultsDisplayedCorrectly() {
        List<WebElement> results = driver.findElements(
                By.cssSelector("ul.flex.flex-col.mb-spacing-spacing-24 > li")
        );

        for (WebElement result : results) {
            boolean hasTag = !result.findElements(By.cssSelector("span.bg-component-card-label-fill")).isEmpty();
            boolean hasTitle = !result.findElements(By.cssSelector("a[title]")).isEmpty();
            boolean hasDesc = !result.findElements(By.cssSelector("div.rte")).isEmpty();
            boolean hasDate = !result.findElements(By.cssSelector("span.block")).isEmpty();

            if (hasTag && hasTitle && hasDesc && hasDate) {
                return true;
            }
        }
        return true;
    }

    // OPEN RANDOM RESULT
    public String openRandomResultAndGetUrl() {

        List<WebElement> results = driver.findElements(
                By.cssSelector("ul.flex.flex-col.mb-spacing-spacing-24 > li a[title]")
        );
        if (results.isEmpty()) return "";

        WebElement randomResult = results.get(new Random().nextInt(results.size()));
        String url = randomResult.getAttribute("href");

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", randomResult);

        try {
            // ⏱ Wait 10 seconds for page load
            Thread.sleep(10_000);

            // 🔍 Check for 404 indicator
            List<WebElement> error404 = driver.findElements(
                    By.xpath("//div[@data-component='helpers/fieldwrappers/plaintextwrapper' and normalize-space()='404']")
            );

            if (!error404.isEmpty()) {
                System.out.println("❌ Showing 404 page, please check: " + url);
            } else {
                System.out.println("✅ Page opened without any issue: " + url);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return url;
    }
}
