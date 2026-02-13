package webpages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ValidationResultTracker;

import java.time.Duration;
import java.util.Set;

public class Footer {

    private WebDriver driver;
    private WebDriverWait wait;

    public Footer(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ================= FOOTER CONTAINER =================
    private By footerContainer = By.tagName("footer");

    // ================= INDUSTRIES =================
    private By aerospaceAndDefenseLink = By.cssSelector("a[href='/en/industries/aerospace-and-defense']");
    private By energyUtilitiesLink = By.cssSelector("a[href='/en/industries/energy-utilities-and-resources']");
    private By constructionEngineeringLink = By.cssSelector("a[href='/en/industries/construction-and-engineering']");
    private By manufacturingLink = By.cssSelector("a[href='/en/industries/manufacturing']");
    private By serviceIndustriesLink = By.cssSelector("a[href='/en/industries/service']");
    private By telecommunicationsLink = By.cssSelector("a[href='/en/industries/telecommunications']");

    // ================= PRODUCTS =================
    private By ifsCloudLink = By.cssSelector("a[href='/en/ifs-cloud']");
    private By erpLink = By.cssSelector("a[href='/en/products/erp']");
    private By eamLink = By.cssSelector("a[href='/en/products/alm/eam']");
    private By fsmLink = By.cssSelector("a[href='/en/products/fsm']");
    private By esmLink = By.cssSelector("a[href='/en/products/esm']");

    // ================= ABOUT =================
    private By aboutUsLink = By.cssSelector("a[href='/en/about']");
    private By careersLink = By.cssSelector("a[href='/en/about/careers']");
    private By newsLink = By.cssSelector("a[href='/en/insights/news']");
    private By contactUsLink = By.cssSelector("a[href='/en/contact-us']");
    private By financialInformationLink = By.cssSelector("a[href='/en/about/financial-information']");
    private By trustCenterLink = By.cssSelector("a[href='/en/about/trust-center']");

    // ================= CUSTOMERS & PARTNERS =================
    private By customerStoriesLink = By.cssSelector("a[href='/en/insights/customer-stories']");
    private By findPartnerLink = By.cssSelector("a[href='/en/partners/find-a-partner']");
    private By becomePartnerLink = By.cssSelector("a[href='/en/partners/become-a-partner']");

    // ================= LEGAL =================
    private By legalLink = By.cssSelector("a[href='/en/legal']");
    private By modernSlaveryActLink = By.cssSelector("a[href='/en/legal/modern-slavery-act']");
    private By privacyLink = By.cssSelector("a[href='/en/legal/privacy']");

    // ================= SOCIAL ICONS =================
    private By facebookIcon = By.cssSelector("a[href='https://www.facebook.com/IFSdotcom']");
    private By twitterIcon = By.cssSelector("a[href='https://x.com/IFS']");
    private By linkedinIcon = By.cssSelector("a[href='https://www.linkedin.com/company/ifs/']");
    private By instagramIcon = By.cssSelector("a[href='https://www.instagram.com/ifs.ai/']");
    private By youtubeIcon = By.cssSelector("a[href='https://www.youtube.com/@IFSdotcom']");

    // ================= PUBLIC METHOD =================
    public void validateAllFooterLinks() {

        long startTime = System.currentTimeMillis();

        try{


        clickAndValidateInternalLink(aerospaceAndDefenseLink);
        clickAndValidateInternalLink(energyUtilitiesLink);
        clickAndValidateInternalLink(constructionEngineeringLink);
        clickAndValidateInternalLink(manufacturingLink);
        clickAndValidateInternalLink(serviceIndustriesLink);
        clickAndValidateInternalLink(telecommunicationsLink);

        clickAndValidateInternalLink(ifsCloudLink);
        clickAndValidateInternalLink(erpLink);
        clickAndValidateInternalLink(eamLink);
        clickAndValidateInternalLink(fsmLink);
        clickAndValidateInternalLink(esmLink);

        clickAndValidateInternalLink(aboutUsLink);
        clickAndValidateInternalLink(careersLink);
        clickAndValidateInternalLink(newsLink);
        clickAndValidateInternalLink(contactUsLink);
        clickAndValidateInternalLink(financialInformationLink);
        clickAndValidateInternalLink(trustCenterLink);

        clickAndValidateInternalLink(customerStoriesLink);
        clickAndValidateInternalLink(findPartnerLink);
        clickAndValidateInternalLink(becomePartnerLink);

        clickAndValidateInternalLink(legalLink);
        clickAndValidateInternalLink(modernSlaveryActLink);
        clickAndValidateInternalLink(privacyLink);

        // Social icons
        validateSocialIcon(facebookIcon, "facebook.com");
        validateSocialIcon(twitterIcon, "x.com");
        validateSocialIcon(linkedinIcon, "linkedin.com");
        validateSocialIcon(instagramIcon, "instagram.com");
        validateSocialIcon(youtubeIcon, "youtube.com");

            ValidationResultTracker.recordPass("Footer");

        } catch (Exception e) {
            ValidationResultTracker.recordFail("Footer");
            throw e;
        } finally {
            ValidationResultTracker.addExecutionTime(
                    "Footer",
                    System.currentTimeMillis() - startTime
            );
        }

    }

    // ================= INTERNAL LINK VALIDATION =================
    private void clickAndValidateInternalLink(By locator) {

        long startTime = System.currentTimeMillis();

        try{

        wait.until(ExpectedConditions.presenceOfElementLocated(footerContainer));
        WebElement link = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

        scrollToElement(link);

        String expectedUrl = link.getAttribute("href");
        System.out.println("🔗 Expected URL: " + expectedUrl);

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        wait.until(ExpectedConditions.urlContains("/en/"));

        String actualUrl = driver.getCurrentUrl();
        System.out.println("🌐 Actual URL:   " + actualUrl);

        boolean is404 = is404Page();

        if (is404) {
            throw new AssertionError("❌ URL FAILED – 404 page detected");
        } else {
            System.out.println("✅ URL OK\n");
        }

        driver.get("https://www.ifs.com/en");
        wait.until(ExpectedConditions.presenceOfElementLocated(footerContainer));



        ValidationResultTracker.recordPass("Footer");

        } catch (Exception e) {
            ValidationResultTracker.recordFail("Footer");
            throw e;
        } finally {
            ValidationResultTracker.addExecutionTime(
                    "Footer",
                    System.currentTimeMillis() - startTime
            );
        }


    }

    // ================= SOCIAL ICON VALIDATION =================
    private void validateSocialIcon(By locator, String expectedDomain) {

        long startTime = System.currentTimeMillis();

        try{

        WebElement icon = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        scrollToElement(icon);

        String originalWindow = driver.getWindowHandle();
        Set<String> oldWindows = driver.getWindowHandles();

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", icon);

        wait.until(d -> driver.getWindowHandles().size() > oldWindows.size());

        for (String window : driver.getWindowHandles()) {
            if (!window.equals(originalWindow)) {
                driver.switchTo().window(window);
                break;
            }
        }

        wait.until(ExpectedConditions.urlContains(expectedDomain));

        System.out.println("🌐 Social URL:   " + driver.getCurrentUrl());
        is404Page();

        driver.close();
        driver.switchTo().window(originalWindow);

            ValidationResultTracker.recordPass("Footer");

        } catch (Exception e) {
            ValidationResultTracker.recordFail("Footer");
            throw e;
        } finally {
            ValidationResultTracker.addExecutionTime(
                    "Footer",
                    System.currentTimeMillis() - startTime
            );
        }



    }

    // ================= 404 STATUS CHECK =================
    private boolean is404Page() {

        long startTime = System.currentTimeMillis();

        try{

        String title = driver.getTitle().toLowerCase();
        String url = driver.getCurrentUrl().toLowerCase();
        String body = driver.findElement(By.tagName("body")).getText().toLowerCase();

        boolean is404 =
                title.contains("404") ||
                        title.contains("not found") ||
                        body.contains("page not found") ||
                        body.contains("404");

        System.out.println("🚦 404 Status:   " + (is404 ? "FOUND ❌ YES" : "NOT FOUND ❌ NO"));

        return is404;

        } catch (Exception e) {
            ValidationResultTracker.recordFail("Footer");
            throw e;
        } finally {
            ValidationResultTracker.addExecutionTime(
                    "Footer",
                    System.currentTimeMillis() - startTime
            );
        }
    }

    // ================= SCROLL HELPER =================
    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollBy(0, -120);");
    }
}
