package webpages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import utils.ValidationResultTracker;

import java.time.Duration;

public class GatedNonGatedAssets {

    private WebDriver driver;
    private WebDriverWait wait;

    public GatedNonGatedAssets(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    /* ===================== GATED ASSET LOCATORS ===================== */

    private By email =
            By.id("email-6bf22320-de35-46fb-82e3-be294cfa3175");

    private By firstName =
            By.id("firstname-6bf22320-de35-46fb-82e3-be294cfa3175");

    private By lastName =
            By.id("lastname-6bf22320-de35-46fb-82e3-be294cfa3175");

    private By jobTitle =
            By.id("jobtitle-6bf22320-de35-46fb-82e3-be294cfa3175");

    private By company =
            By.id("company-6bf22320-de35-46fb-82e3-be294cfa3175");

    private By countryDropdown =
            By.id("country_dropdown-6bf22320-de35-46fb-82e3-be294cfa3175");

    private By consentCheckbox =
            By.id("LEGAL_CONSENT.subscription_type_1432764756-6bf22320-de35-46fb-82e3-be294cfa3175");

    private By downloadButton =
            By.xpath("//button[.//text()[contains(.,'Download')]]");

    private By successMessage =
            By.cssSelector("div.submitted-message.hs-main-font-element span");

    /* ===================== NON-GATED ASSET LOCATOR ===================== */

    private By nonGatedDownloadButton =
            By.xpath("//a[@title='Download eBook' and contains(@href,'.pdf')]");

    /* ===================== GATED FORM ===================== */

    public void fillGatedForm() {

        long startTime = System.currentTimeMillis();

        try {

            wait.until(ExpectedConditions.visibilityOfElementLocated(email))
                    .sendKeys("test@testtest.com");

            driver.findElement(firstName).sendKeys("Test User");
            driver.findElement(lastName).sendKeys("Test");
            driver.findElement(jobTitle).sendKeys("Engineer");
            driver.findElement(company).sendKeys("Test Company ABC");

            Select select = new Select(driver.findElement(countryDropdown));
            select.selectByValue("CA");

            WebElement checkbox = driver.findElement(consentCheckbox);
            if (!checkbox.isSelected()) {
                ((JavascriptExecutor) driver)
                        .executeScript("arguments[0].click();", checkbox);
            }

            System.out.println("📝 Gated Asset Form Submitted Successfully");

            ValidationResultTracker.recordPass("GatedNonGatedAssetDownload");

        } catch (Exception e) {

            ValidationResultTracker.recordFail("GatedNonGatedAssetDownload");
            throw e;

        } finally {

            ValidationResultTracker.addExecutionTime(
                    "GatedNonGatedAssetDownload",
                    System.currentTimeMillis() - startTime
            );
        }
    }

    /* ===================== GATED DOWNLOAD ===================== */

    public String downloadGatedAssetAndCapturePdfUrl() {

        long startTime = System.currentTimeMillis();

        try {

            String parentWindow = driver.getWindowHandle();
            int initialWindows = driver.getWindowHandles().size();

            wait.until(ExpectedConditions.elementToBeClickable(downloadButton)).click();

            wait.until(d -> d.getWindowHandles().size() > initialWindows);

            String pdfUrl = "NOT_LOADED";

            for (String win : driver.getWindowHandles()) {
                if (!win.equals(parentWindow)) {
                    driver.switchTo().window(win);
                    break;
                }
            }

            for (int i = 0; i < 10; i++) {
                String url = driver.getCurrentUrl();
                if (!url.equalsIgnoreCase("about:blank")) {
                    pdfUrl = url;
                    break;
                }
                Thread.sleep(500);
            }

            System.out.println("📄 Gated Asset PDF URL: " + pdfUrl);

            driver.close();
            driver.switchTo().window(parentWindow);

            ValidationResultTracker.recordPass("GatedNonGatedAssetDownload");

            return pdfUrl;

        } catch (Exception e) {

            ValidationResultTracker.recordFail("GatedNonGatedAssetDownload");
            throw new RuntimeException(e);

        } finally {

            ValidationResultTracker.addExecutionTime(
                    "GatedNonGatedAssetDownload",
                    System.currentTimeMillis() - startTime
            );
        }
    }

    /* ===================== SUCCESS MESSAGE ===================== */

    public String getGatedSuccessMessage() {

        long startTime = System.currentTimeMillis();

        try {

            String message = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(successMessage)
            ).getText();

            System.out.println("✅ Gated Success Message: " + message);

            ValidationResultTracker.recordPass("GatedNonGatedAssetDownload");

            return message;

        } catch (Exception e) {

            ValidationResultTracker.recordFail("GatedNonGatedAssetDownload");
            throw e;

        } finally {

            ValidationResultTracker.addExecutionTime(
                    "GatedNonGatedAssetDownload",
                    System.currentTimeMillis() - startTime
            );
        }
    }

    /* ===================== NON-GATED DOWNLOAD ===================== */

    public String downloadNonGatedAssetAndCapturePdfUrl() {

        long startTime = System.currentTimeMillis();

        try {

            WebElement downloadBtn =
                    wait.until(ExpectedConditions.elementToBeClickable(nonGatedDownloadButton));

            String expectedPdfUrl = downloadBtn.getAttribute("href");
            System.out.println("📎 Expected PDF URL: " + expectedPdfUrl);

            downloadBtn.click();

            wait.until(driver ->
                    driver.getCurrentUrl().toLowerCase().contains(".pdf")
            );

            String actualPdfUrl = driver.getCurrentUrl();
            System.out.println("📄 Non-Gated Asset PDF URL: " + actualPdfUrl);

            ValidationResultTracker.recordPass("GatedNonGatedAssetDownload");

            return actualPdfUrl;

        } catch (Exception e) {

            ValidationResultTracker.recordFail("GatedNonGatedAssetDownload");
            throw e;

        } finally {

            ValidationResultTracker.addExecutionTime(
                    "GatedNonGatedAssetDownload",
                    System.currentTimeMillis() - startTime
            );
        }
    }
}
