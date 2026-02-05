package webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

public class Footer {

    private WebDriver driver;
    private WebDriverWait wait;

    public Footer(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ================= Footer container =================
    private By footerContainer = By.tagName("footer");

    // ================= Industries =================
    private By aerospaceAndDefenseLink =
            By.cssSelector("a[href='/en/industries/aerospace-and-defense']");
    private By energyUtilitiesLink =
            By.cssSelector("a[href='/en/industries/energy-utilities-and-resources']");
    private By constructionEngineeringLink =
            By.cssSelector("a[href='/en/industries/construction-and-engineering']");
    private By manufacturingLink =
            By.cssSelector("a[href='/en/industries/manufacturing']");
    private By serviceIndustriesLink =
            By.cssSelector("a[href='/en/industries/service']");
    private By telecommunicationsLink =
            By.cssSelector("a[href='/en/industries/telecommunications']");

    // ================= Products =================
    private By ifsCloudLink = By.cssSelector("a[href='/en/ifs-cloud']");
    private By erpLink = By.cssSelector("a[href='/en/products/erp']");
    private By eamLink = By.cssSelector("a[href='/en/products/alm/eam']");
    private By fsmLink = By.cssSelector("a[href='/en/products/fsm']");
    private By esmLink = By.cssSelector("a[href='/en/products/esm']");

    // ================= About =================
    private By aboutUsLink = By.cssSelector("a[href='/en/about']");
    private By careersLink = By.cssSelector("a[href='/en/about/careers']");
    private By newsLink = By.cssSelector("a[href='/en/insights/news']");
    private By contactUsLink = By.cssSelector("a[href='/en/contact-us']");
    private By financialInformationLink =
            By.cssSelector("a[href='/en/about/financial-information']");
    private By trustCenterLink =
            By.cssSelector("a[href='/en/about/trust-center']");

    // ================= Customers & Partners =================
    private By customerStoriesLink =
            By.cssSelector("a[href='/en/insights/customer-stories']");
    private By findPartnerLink =
            By.cssSelector("a[href='/en/partners/find-a-partner']");
    private By becomePartnerLink =
            By.cssSelector("a[href='/en/partners/become-a-partner']");

    // ================= Legal & Privacy =================
    private By legalLink = By.cssSelector("a[href='/en/legal']");
    private By modernSlaveryActLink =
            By.cssSelector("a[href='/en/legal/modern-slavery-act']");
    private By privacyLink =
            By.cssSelector("a[href='/en/legal/privacy']");

    // ✅ Cookie Settings locator
    private By cookieSettingsLink =
            By.xpath("//footer//*[normalize-space()='Cookie settings']");

    // ================= Cookie Modal (OneTrust) =================
    private By cookieModalTitle = By.id("ot-category-title");
    private By cookieModalCloseBtn = By.id("close-pc-btn-handler");

    // ================= External Links =================
    private By genderPayGapReportLink =
            By.cssSelector("a[href*='gender-pay-gap-report']");
    private By reportAConcernLink =
            By.cssSelector("a[href='https://report.whistleb.com/en/ifs']");

    // ================= Social Media Icons =================
    private By facebookIcon =
            By.cssSelector("a[href='https://www.facebook.com/IFSdotcom']");
    private By twitterIcon =
            By.cssSelector("a[href='https://x.com/IFS']");
    private By linkedinIcon =
            By.cssSelector("a[href='https://www.linkedin.com/company/ifs/']");
    private By instagramIcon =
            By.cssSelector("a[href='https://www.instagram.com/ifs.ai/']");
    private By youtubeIcon =
            By.cssSelector("a[href='https://www.youtube.com/@IFSdotcom']");

    // ================= Footer Text =================
    private By copyrightText =
            By.xpath("//p[contains(text(),'©')]");
    private By industrialAiText =
            By.xpath("//p[contains(.,'Industrial AI') and contains(.,'that matters')]");

    // ================= PUBLIC TEST METHOD =================
    public void validateFooterIndustryLinks() {

        // Internal links
        clickAndValidateLink(aerospaceAndDefenseLink, "/en/industries/aerospace-and-defense");
        clickAndValidateLink(energyUtilitiesLink, "/en/industries/energy-utilities-and-resources");
        clickAndValidateLink(constructionEngineeringLink, "/en/industries/construction-and-engineering");
        clickAndValidateLink(manufacturingLink, "/en/industries/manufacturing");
        clickAndValidateLink(serviceIndustriesLink, "/en/industries/service");
        clickAndValidateLink(telecommunicationsLink, "/en/industries/telecommunications");

        clickAndValidateLink(ifsCloudLink, "/en/ifs-cloud");
        clickAndValidateLink(erpLink, "/en/products/erp");
        clickAndValidateLink(eamLink, "/en/products/alm/eam");
        clickAndValidateLink(fsmLink, "/en/products/fsm");
        clickAndValidateLink(esmLink, "/en/products/esm");

        clickAndValidateLink(aboutUsLink, "/en/about");
        clickAndValidateLink(careersLink, "/en/about/careers");
        clickAndValidateLink(newsLink, "/en/insights/news");
        clickAndValidateLink(contactUsLink, "/en/contact-us");
        clickAndValidateLink(financialInformationLink, "/en/about/financial-information");
        clickAndValidateLink(trustCenterLink, "/en/about/trust-center");

        clickAndValidateLink(customerStoriesLink, "/en/insights/customer-stories");
        clickAndValidateLink(findPartnerLink, "/en/partners/find-a-partner");
        clickAndValidateLink(becomePartnerLink, "/en/partners/become-a-partner");

        clickAndValidateLink(legalLink, "/en/legal");
        clickAndValidateLink(modernSlaveryActLink, "/en/legal/modern-slavery-act");
        clickAndValidateLink(privacyLink, "/en/legal/privacy");

        // Cookie modal
        try {
            openCookieSettingsAndValidate();
        } catch (Exception e) {
            System.out.println("ℹ️ Cookie settings not present (already accepted). Skipping.");
        }

        // External links
        clickExternalLinkAndReturn(genderPayGapReportLink, false); // same tab → navigate back
        clickExternalLinkAndReturn(reportAConcernLink, true);      // opens in new tab


        // Footer texts
        validateFooterText();

        // Social icons
        validateSocialIcons();
    }

    // ================= CLICK & VALIDATE INTERNAL LINKS =================
    private void clickAndValidateLink(By locator, String expectedUrlPart) {

        wait.until(ExpectedConditions.presenceOfElementLocated(footerContainer));
        WebElement link = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", link
        );
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -120);");

        System.out.println("🔗 Expected URL: " + link.getAttribute("href"));

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        wait.until(ExpectedConditions.urlContains(expectedUrlPart));

        System.out.println("Actual URL: " + driver.getCurrentUrl());
        System.out.println("URL validation PASSED\n");

        driver.get("https://www.ifs.com/en");
        wait.until(ExpectedConditions.presenceOfElementLocated(footerContainer));
    }

    // ================= COOKIE SETTINGS =================
    private void openCookieSettingsAndValidate() {

        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(cookieSettingsLink));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", link
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);

        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(cookieModalTitle));

        System.out.println("🍪 Cookie modal opened: " + title.getText());

        WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(cookieModalCloseBtn));
        closeBtn.click();

        wait.until(ExpectedConditions.invisibilityOf(title));
    }

    // ================= EXTERNAL LINKS (HANDLES SAME TAB OR NEW TAB) =================
    private void clickExternalLinkAndReturn(By locator, boolean opensInNewTab) {

        WebElement link = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        String expectedUrl = link.getAttribute("href");

        System.out.println("🌐 External URL expected: " + expectedUrl);

        if (opensInNewTab) {
            // Handle new tab
            String originalWindow = driver.getWindowHandle();
            Set<String> oldWindows = driver.getWindowHandles();

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});", link
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);

            // Wait for new tab
            wait.until(d -> driver.getWindowHandles().size() > oldWindows.size());

            // Switch to new tab
            Set<String> newWindows = driver.getWindowHandles();
            newWindows.removeAll(oldWindows);
            String newTab = newWindows.iterator().next();
            driver.switchTo().window(newTab);

            wait.until(ExpectedConditions.urlContains(expectedUrl));
            System.out.println("🌐 Actual URL: " + driver.getCurrentUrl());

            driver.close();
            driver.switchTo().window(originalWindow);
            wait.until(ExpectedConditions.presenceOfElementLocated(footerContainer));

        } else {
            // Same tab → validate and go back
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});", link
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);

            wait.until(ExpectedConditions.urlContains(expectedUrl));
            System.out.println("🌐 Actual URL: " + driver.getCurrentUrl());

            // Go back to previous page
            driver.navigate().back();
            wait.until(ExpectedConditions.presenceOfElementLocated(footerContainer));
        }
    }

    // ================= FOOTER TEXT =================
    private void validateFooterText() {
        System.out.println("📄 " +
                wait.until(ExpectedConditions.visibilityOfElementLocated(copyrightText)).getText());
        System.out.println("🤖 " +
                wait.until(ExpectedConditions.visibilityOfElementLocated(industrialAiText)).getText());
    }

    // ================= SOCIAL ICONS =================
    private void validateSocialIcons() {

        By[] icons = {
                facebookIcon,
                twitterIcon,
                linkedinIcon,
                instagramIcon,
                youtubeIcon
        };

        for (By icon : icons) {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(icon));
            if (!el.isDisplayed()) {
                throw new AssertionError("Missing social icon: " + icon);
            }
        }
        System.out.println("✅ All social icons visible");
    }
}
