package base;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.ConsoleCapture;
import utils.OutlookEmailUtil;

import java.time.Duration;
import java.util.List;

public class BaseTest {

    protected WebDriver driver;
    private static ConsoleCapture consoleCapture = new ConsoleCapture();

    // Will store the REAL executed test class name (SearchTest etc.)
    private static String executedTestClassName = "UnknownTest";

    @BeforeSuite
    public void startConsoleCapture() {
        consoleCapture.start();
        System.out.println("=== Test Run Started ===");
    }

    @BeforeMethod
    public void setUp() {

        System.setProperty(
                "webdriver.edge.driver",
                "C:\\Users\\cogalk\\OneDrive - IFS\\Desktop\\web driver\\msedgedriver.exe"
        );

        driver = new EdgeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        driver.get("https://www.ifs.com");
        System.out.println("Page Title: " + driver.getTitle());

        acceptCookiesIfPresent();
    }

    private void acceptCookiesIfPresent() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
            for (WebElement iframe : iframes) {
                String src = iframe.getAttribute("src");
                if (src != null && src.contains("consent")) {
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
            driver.switchTo().defaultContent();
            System.out.println("⚠️ Cookie banner not shown");
        }
    }

    // Capture REAL test class name here
    @AfterMethod
    public void tearDown(ITestResult result) {

        executedTestClassName =
                result.getTestClass()
                        .getRealClass()
                        .getSimpleName();

        if (driver != null) {
            driver.quit();
            System.out.println("🧹 Browser closed");
        }
    }

    // AFTER SUITE TO SEND EMAIL
    @AfterSuite(alwaysRun = true)
    public void sendTestReportEmail() {

        consoleCapture.stop();
        String fullConsole = consoleCapture.getCapturedOutput();

        StringBuilder htmlBody = new StringBuilder();
        htmlBody.append("<html><body>");
        htmlBody.append("<p>Hi Team,</p>");
        htmlBody.append("<p>The automation execution has completed successfully.</p>");
        htmlBody.append("<p><b>Executed Test Class:</b> ")
                .append(executedTestClassName)
                .append("</p>");
        htmlBody.append("<p>Please find the attached <b>Word report</b> for full execution logs and results.</p>");
        htmlBody.append("<br>");
        htmlBody.append("<p>Regards,<br>Automation Bot</p>");
        htmlBody.append("</body></html>");

        String[] recipients = {
                "gpasindu55@gmail.com",
                "chathurika.senarath@ifs.com",
                "rashmi.sandeepani@ifs.com"
        };

        OutlookEmailUtil.sendEmailWithTempWord(
                recipients,
                "Automation Test Report - " + executedTestClassName,
                htmlBody.toString(),
                fullConsole
        );
    }
}
