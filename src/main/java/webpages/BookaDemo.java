package webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BookaDemo {

    private WebDriver driver;
    private WebDriverWait wait;

    public BookaDemo(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // Accept cookies if popup appears
    public void acceptCookies() {
        try {
            WebElement cookieBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("onetrust-accept-btn-handler"))
            );
            cookieBtn.click();
            System.out.println("✅ Cookies accepted");
        } catch (Exception e) {
            System.out.println("ℹ Cookies not found / already accepted");
        }
    }

    public void clickBookDemoLink() {
        WebElement bookDemoLink = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("a[title='Book a demo']"))
        );
        bookDemoLink.click();
        System.out.println("✅ Clicked on Book a Demo link");
    }

    public void fillDemoForm() {
        // Fill all form fields
        fillInput("email-faeb8764-7d13-42cd-900f-536e9054d1bb", "test1@ifs.com", "Email");
        fillInput("firstname-faeb8764-7d13-42cd-900f-536e9054d1bb", "Test11", "First Name");
        fillInput("lastname-faeb8764-7d13-42cd-900f-536e9054d1bb", "Test23", "Last Name");
        fillInput("jobtitle-faeb8764-7d13-42cd-900f-536e9054d1bb", "Test Engineer", "Job Title");
        fillInput("company-faeb8764-7d13-42cd-900f-536e9054d1bb", "Test Company", "Company");
        fillInput("phone-faeb8764-7d13-42cd-900f-536e9054d1bb", "123456789", "Phone");

        // Select Country
        WebElement countryDropdown = driver.findElement(By.id("country_dropdown-faeb8764-7d13-42cd-900f-536e9054d1bb"));
        Select countrySelect = new Select(countryDropdown);
        List<WebElement> countryOptions = countrySelect.getOptions();
        System.out.println("🌍 Country Options:");
        for (WebElement option : countryOptions) {
            System.out.println(" - " + option.getText());
        }
        countrySelect.selectByIndex(1); // select first country after "Please Select"
        System.out.println("✅ Selected Country: " + countrySelect.getFirstSelectedOption().getText());

        // Select Solution Interest
        WebElement solutionDropdown = driver.findElement(By.id("solution_interest-faeb8764-7d13-42cd-900f-536e9054d1bb"));
        Select solutionSelect = new Select(solutionDropdown);
        List<WebElement> solutionOptions = solutionSelect.getOptions();
        System.out.println("🌟 Solution Options:");
        for (WebElement option : solutionOptions) {
            System.out.println(" - " + option.getText());
        }
        solutionSelect.selectByIndex(1); // select first solution option
        System.out.println("✅ Selected Solution Interest: " + solutionSelect.getFirstSelectedOption().getText());

        // Fill Message
        fillInput("message-faeb8764-7d13-42cd-900f-536e9054d1bb", "Test scenario", "Message");

        // Check Consent
        WebElement consent = driver.findElement(
                By.id("LEGAL_CONSENT.subscription_type_1432764756-faeb8764-7d13-42cd-900f-536e9054d1bb")
        );
        if (!consent.isSelected()) {
            consent.click();
        }
        System.out.println("✅ Checked Consent Checkbox");
    }

    private void fillInput(String id, String value, String fieldName) {
        WebElement input = driver.findElement(By.id(id));
        input.clear();
        input.sendKeys(value);
        System.out.println("✅ Entered " + fieldName + ": " + value);
    }

    public void submitForm() {
        WebElement submitButton = driver.findElement(
                By.cssSelector("button.flex-row.flex.items-center.justify-center.gap-x-spacing-spacing-8")
        );

        // Scroll a little above the button to avoid sticky header overlapping
        ((JavascriptExecutor) driver).executeScript(
                "window.scrollBy(0, arguments[0].getBoundingClientRect().top - 100);", submitButton
        );

        // Optional: wait a tiny bit to ensure button is ready
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        // Click using JS to bypass overlay issues
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);

        System.out.println("✅ Clicked Submit Button via JS after scrolling");
    }


    public boolean isThankYouMessageDisplayed() {
        try {
            // Wait for up to 30s to handle animations / overlays
            WebElement thankYouMessage = new WebDriverWait(driver, Duration.ofSeconds(30))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.submitted-message")));

            // Scroll into view to bypass sticky header
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", thankYouMessage);

            return thankYouMessage.isDisplayed();
        } catch (Exception e) {
            System.out.println("❌ Thank You message not found: " + e.getMessage());
            return false;
        }
    }
}
