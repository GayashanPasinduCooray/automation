package webpages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ValidationResultTracker;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;


public class Homepage {

    private WebDriver driver;
    private WebDriverWait wait;

    public Homepage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /* ================= HERO BANNER ================= */

    private By heroBannerTitle =
            By.xpath("//h2[contains(text(),'IFS and Cadillac Formula 1')]");

    private By heroLearnMoreBtn =
            By.xpath("//a[@title='Learn more']");

    public void validateHeroBanner() {
        System.out.println("===== Hero Banner Component Validation =====");


        long startTime = System.currentTimeMillis();

        try {
            WebElement title =
                    wait.until(ExpectedConditions.visibilityOfElementLocated(heroBannerTitle));
            System.out.println("Hero Banner Text: " + title.getText());

            WebElement btn =
                    wait.until(ExpectedConditions.elementToBeClickable(heroLearnMoreBtn));
            btn.click();

            waitFor(5);
            System.out.println("Hero Learn More URL: " + driver.getCurrentUrl());

            ValidationResultTracker.recordPass("Homepage");

        } catch (Exception e) {
            ValidationResultTracker.recordFail("Homepage");
            throw e;
        } finally {
            ValidationResultTracker.addExecutionTime(
                    "Homepage",
                    System.currentTimeMillis() - startTime
            );
        }

    }

    /* ================= INTRO TEXT ================= */

    private By introTitle =
            By.xpath("//h1[text()='AI-Powered Software Built for Your Industry']");

    public void validateIntroText() {
        System.out.println("===== Intro Text Component Validation =====");

        long startTime = System.currentTimeMillis();

        try{

        WebElement intro =
                wait.until(ExpectedConditions.visibilityOfElementLocated(introTitle));
        System.out.println("Intro Text: " + intro.getText());

            ValidationResultTracker.recordPass("Homepage");

        } catch (Exception e) {
            ValidationResultTracker.recordFail("Homepage");
            throw e;
        } finally {
            ValidationResultTracker.addExecutionTime(
                    "Homepage",
                    System.currentTimeMillis() - startTime
            );
        }
    }

    /* ================= 1ST PROMO ================= */

    private By promoTitle =
            By.xpath("//h2[contains(text(),'Industrial AI is IFS.ai')]");

    private By promoDesc =
            By.xpath("//div[@class='ck-content']//p");

    private By promoBtn =
            By.xpath("//a[@title='Discover Industrial AI']");

    public void validateFirstPromo() {
        System.out.println("===== 1st Promo Component Validation =====");

        long startTime = System.currentTimeMillis();

        try{

        WebElement title =
                wait.until(ExpectedConditions.visibilityOfElementLocated(promoTitle));
        System.out.println("Promo Title: " + title.getText());

        WebElement desc =
                wait.until(ExpectedConditions.visibilityOfElementLocated(promoDesc));
        System.out.println("Promo Description: " + desc.getText());

        WebElement btn =
                wait.until(ExpectedConditions.elementToBeClickable(promoBtn));
        btn.click();

        waitFor(5);
        System.out.println("Discover Industrial AI URL: " + driver.getCurrentUrl());

        ValidationResultTracker.recordPass("Homepage");

        } catch (Exception e) {
            ValidationResultTracker.recordFail("Homepage");
            throw e;
        } finally {
            ValidationResultTracker.addExecutionTime(
                    "Homepage",
                    System.currentTimeMillis() - startTime
            );
        }

    }

    /* ================= NAVIGATE BACK TO HOME ================= */

    public void navigateBackToHome() {
        driver.navigate().to("https://www.ifs.com/en");
        waitFor(5);
    }

    /* ================= ACCORDION ================= */

    private By accordionTitle =
            By.xpath("//h2//span[text()='Industrial AI']");

    private By accordionItem =
            By.xpath("//h3[text()='Construction & Engineering']");

    private By accordionImage =
            By.xpath("//img[contains(@src,'ifs_icons_general_bridge')]");

    private By accordionDesc =
            By.xpath("//div[contains(@class,'icon-grid-description')]");

    private By exploreIndustriesBtn =
            By.xpath("//a[@title='Explore Industries']");

    public void validateAccordionComponent() {
        System.out.println("===== Accordion Component Validation =====");

        long startTime = System.currentTimeMillis();

        try{


        WebElement title =
                wait.until(ExpectedConditions.visibilityOfElementLocated(accordionTitle));
        System.out.println("Accordion Title: " + title.getText());

        WebElement item =
                wait.until(ExpectedConditions.elementToBeClickable(accordionItem));
        item.click();

        WebElement image =
                wait.until(ExpectedConditions.presenceOfElementLocated(accordionImage));

        Boolean loaded = (Boolean) ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].complete && arguments[0].naturalWidth > 0;", image);

        System.out.println("Accordion Image Loaded: " + loaded);

        WebElement desc =
                wait.until(ExpectedConditions.presenceOfElementLocated(accordionDesc));
        System.out.println("Accordion Description: " + desc.getText());

        WebElement btn =
                wait.until(ExpectedConditions.elementToBeClickable(exploreIndustriesBtn));
        btn.click();

        waitFor(5);
        System.out.println("Explore Industries URL: " + driver.getCurrentUrl());

            ValidationResultTracker.recordPass("Homepage");

        } catch (Exception e) {
            ValidationResultTracker.recordFail("Homepage");
            throw e;
        } finally {
            ValidationResultTracker.addExecutionTime(
                    "Homepage",
                    System.currentTimeMillis() - startTime
            );
        }


    }


    /* ================= MEDIA CAROUSEL ================= */
    /* ================= MEDIA CAROUSEL ================= */
    public void validateMediaCarousel() {

        long start = System.currentTimeMillis();   // ⏱ START TIME
        System.out.println("Media Carousel Validate");

        try {

            // Scroll to carousel
            WebElement carouselHeader = driver.findElement(
                    By.xpath("//h2[contains(text(),'Products Tailored')]"));
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView(true);", carouselHeader);

            Thread.sleep(2000);

            System.out.println("Carousel Header: " + carouselHeader.getText());

            WebElement carouselContainer = driver.findElement(
                    By.cssSelector("#splide02-list"));

            Set<String> capturedTitles = new LinkedHashSet<>();

            long startTime = System.currentTimeMillis();
            long maxDuration = 45000;

            while (System.currentTimeMillis() - startTime < maxDuration) {

                try {
                    WebElement activeSlide = carouselContainer.findElement(
                            By.cssSelector("li.splide__slide.is-active"));

                    String title = "";
                    String desc = "";

                    try {
                        title = activeSlide.findElement(
                                        By.cssSelector("h3.rte.text-component-carousel-title"))
                                .getText().trim();
                    } catch (Exception ignored) {}

                    try {
                        desc = activeSlide.findElement(
                                        By.cssSelector("span.carousel-rich-text"))
                                .getText().trim();
                    } catch (Exception ignored) {}

                    if (!title.isEmpty() && capturedTitles.add(title)) {
                        System.out.println("Carousel Item "
                                + capturedTitles.size() + " Title: " + title);
                        System.out.println("Carousel Item Description: " + desc);
                        System.out.println("--------------------------------------------");
                    }

                } catch (Exception ignored) {}

                Thread.sleep(1500);
            }

            int totalCaptured = capturedTitles.size();
            System.out.println("Total carousel items captured: " + totalCaptured);

            /* ================= SMART ASSERTION ================= */

            int MIN_EXPECTED_SLIDES = 2;   // ✅ configurable

            if (totalCaptured < MIN_EXPECTED_SLIDES) {
                throw new RuntimeException(
                        "Carousel validation failed. Expected at least "
                                + MIN_EXPECTED_SLIDES
                                + " items, but found " + totalCaptured);
            }

            // ✅ PASS
            ValidationResultTracker.recordPass("Homepage");

        } catch (Exception e) {

            // ❌ FAIL
            ValidationResultTracker.recordFail("Homepage");
            throw new RuntimeException(e);

        } finally {

            // ⏱ ALWAYS RECORD TIME
            ValidationResultTracker.addExecutionTime(
                    "Homepage",
                    System.currentTimeMillis() - start
            );
        }
    }











    /* ================= FACT SHEET ================= */

    public void validateFactSheet() {
        System.out.println("Fact sheet validate");

        try {
            System.out.println(driver.findElement(
                    By.xpath("//h2[contains(text(),'Trusted. Proven')]")).getText());

            System.out.println(driver.findElement(
                    By.xpath("//p[contains(text(),'Showcasing global reach')]")).getText());

            // ✅ Count fact sheets by TITLE (correct & stable)
            List<WebElement> factTitles = driver.findElements(
                    By.xpath("//p[contains(@class,'text-component-stat-label')]"));

            System.out.println("Fact Sheet Count: " + factTitles.size());

            for (WebElement title : factTitles) {
                System.out.println("Fact Sheet Title: " + title.getText());

                WebElement parentCard = title.findElement(By.xpath("./ancestor::li"));

                String number = parentCard.findElement(
                                By.xpath(".//span[contains(@class,'font-typography-font-family-header')]"))
                        .getText();

                String description = parentCard.findElement(
                                By.xpath(".//p[contains(@class,'text-component-stat-description')]"))
                        .getText();

                System.out.println("Fact Sheet Number: " + number);
                System.out.println("Fact Sheet Description: " + description);
                System.out.println("------------------------------------------------");
            }

        } catch (Exception e) {
            System.out.println("Fact sheet content changed or not loaded");
        }
    }

    /* ================= QUOTE ================= */

    public void validateQuoteComponent() {
        System.out.println("Validate Quote component");

        try {
            System.out.println(driver.findElement(
                    By.xpath("//h2[contains(text(),'Trusted by our customers')]")).getText());

            System.out.println(driver.findElement(
                    By.xpath("//span[contains(@class,'quote-quote')]")).getText());

            System.out.println(driver.findElement(
                    By.xpath("//div[contains(@class,'quote-name')]")).getText());

            System.out.println(driver.findElement(
                    By.xpath("//div[contains(@class,'quote-details')]")).getText());
        } catch (Exception e) {
            System.out.println("Quote component content updated");
        }
    }

    /* ================= LOGO CAROUSEL ================= */

    public void validateLogoCarousel() {
        System.out.println("Logo Carousel Validate");

        try {
            // Find all slides
            List<WebElement> slides = driver.findElements(
                    By.xpath("//ul[contains(@class,'splide__list')]/li")
            );



            // Find the last slide that has 'aria-label' with "of" to get real count
            int totalSlides = 0;
            for (WebElement slide : slides) {
                String ariaLabel = slide.getAttribute("aria-label");
                if (ariaLabel != null && ariaLabel.contains("of")) {
                    // Example: "16 of 16" -> split by "of" -> take 2nd part
                    String[] parts = ariaLabel.split("of");
                    totalSlides = Integer.parseInt(parts[1].trim());
                }
            }

            System.out.println("✅ Total unique logos in carousel: " + totalSlides);
        } catch (Exception e) {
            System.out.println("Logo carousel not available: " + e.getMessage());
        }
    }


    /* ================= PROMO COMPONENT 2 ================= */

    public void validateSecondPromo() {
        System.out.println("Promo component 2 validate");

        try {
            System.out.println(driver.findElement(
                    By.xpath("//h2[contains(text(),'Sustainability Report')]")).getText());

            System.out.println(driver.findElement(
                    By.xpath("//div[contains(@class,'promo-subtitle')]")).getText());
        } catch (Exception e) {
            System.out.println("Promo 2 content updated");
        }
    }

    /* ================= NEWS & UPDATES ================= */

    public void validateNewsAndUpdates() {
        System.out.println("News and update component validate");

        try {
            System.out.println(driver.findElement(
                    By.xpath("//h2[contains(text(),'News and updates')]")).getText());

            WebElement articleBtn =
                    driver.findElement(By.xpath("//a[@title='Read article']"));

            articleBtn.click();
            waitFor(5);

            System.out.println("News Article URL: " + driver.getCurrentUrl());

            navigateBackToHome();
        } catch (Exception e) {
            System.out.println("News content updated");
        }
    }

    /* ================= GET STARTED FORM ================= */

    public void validateGetStartedForm() {
        System.out.println("===== Get Started Form Validation =====");

        // Validate "Get started with IFS" title
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[.//span[text()='Get started']]")));
        System.out.println("Section Title: " + title.getText());

        // Validate description text
        WebElement desc = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'ck-content')]//p[1]")));
        System.out.println("Description 1: " + desc.getText());

        WebElement desc2 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'ck-content')]//p[2]")));
        System.out.println("Description 2: " + desc2.getText());

        // Email
        WebElement email = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("email-647bf904-d7ea-4e91-9a20-ddc645ff146a")));
        email.sendKeys("test11@ifs.com");
        System.out.println("Entered Email: " + email.getAttribute("value"));

        // First name
        WebElement firstName = driver.findElement(
                By.id("firstname-647bf904-d7ea-4e91-9a20-ddc645ff146a"));
        firstName.sendKeys("test bot");
        System.out.println("Entered First Name: " + firstName.getAttribute("value"));

        // Last name
        WebElement lastName = driver.findElement(
                By.id("lastname-647bf904-d7ea-4e91-9a20-ddc645ff146a"));
        lastName.sendKeys("automation test");
        System.out.println("Entered Last Name: " + lastName.getAttribute("value"));

        // Job title
        WebElement jobTitle = driver.findElement(
                By.id("jobtitle-647bf904-d7ea-4e91-9a20-ddc645ff146a"));
        jobTitle.sendKeys("test engineer");
        System.out.println("Entered Job Title: " + jobTitle.getAttribute("value"));

        // Company
        WebElement company = driver.findElement(
                By.id("company-647bf904-d7ea-4e91-9a20-ddc645ff146a"));
        company.sendKeys("test company");
        System.out.println("Entered Company: " + company.getAttribute("value"));

        // Phone
        WebElement phone = driver.findElement(
                By.id("phone-647bf904-d7ea-4e91-9a20-ddc645ff146a"));
        phone.sendKeys("1234567890");
        System.out.println("Entered Phone: " + phone.getAttribute("value"));

        // Country dropdown – validate all & select random
        WebElement countryDropdown = driver.findElement(
                By.id("country_dropdown-647bf904-d7ea-4e91-9a20-ddc645ff146a"));
        countryDropdown.click();

        List<WebElement> countries =
                driver.findElements(By.xpath("//select[@id='country_dropdown-647bf904-d7ea-4e91-9a20-ddc645ff146a']/option"));

        System.out.println("Total countries available: " + countries.size());

        for (WebElement country : countries) {
            System.out.println("Country: " + country.getText());
        }

        // Select random country (skip placeholder)
        int randomIndex = new java.util.Random().nextInt(countries.size() - 1) + 1;
        countries.get(randomIndex).click();
        System.out.println("Selected Country: " + countries.get(randomIndex).getText());

        // Message
        WebElement message = driver.findElement(
                By.id("message-647bf904-d7ea-4e91-9a20-ddc645ff146a"));
        message.sendKeys("test");
        System.out.println("Entered Message: " + message.getAttribute("value"));

        // Consent checkbox
        WebElement consentCheckbox = driver.findElement(
                By.id("LEGAL_CONSENT.subscription_type_1432764756-647bf904-d7ea-4e91-9a20-ddc645ff146a"));
        if (!consentCheckbox.isSelected()) {
            consentCheckbox.click();
            System.out.println("Consent Checkbox selected.");

        }

        // Submit button
        // Locate the visible "Contact Us" button by text
        WebElement submitBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[.//text()[contains(.,'Contact Us')]]")
        ));

        // Scroll to center to avoid sticky overlays
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", submitBtn
        );

        // Extra safety wait for HubSpot reflow
        waitFor(1);

        // Click via JavaScript (most reliable for HubSpot)
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();", submitBtn
        );

        System.out.println("✅ Contact Us button clicked via JavaScript");



        // Wait 10 seconds
        waitFor(10);

        // Validate success message
        WebElement successMsg = new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("div.submitted-message")));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", successMsg);




        System.out.println("Form Submission Message: " + successMsg.getText());
    }


    /* ================= UTILITY ================= */

    private void waitFor(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException ignored) {
        }
    }
}
