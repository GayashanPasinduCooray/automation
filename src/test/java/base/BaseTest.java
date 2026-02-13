package base;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.ConsoleCapture;
import utils.OutlookEmailUtil;
import utils.ExcelReportUtil;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BaseTest {

    protected WebDriver driver;
    private static ConsoleCapture consoleCapture = new ConsoleCapture();

    // Will store all executed test class names
    private static Set<String> executedTestClasses = new LinkedHashSet<>();

    // Store pass/fail count per class
    private static Map<String, Map<String, Integer>> testResults = new HashMap<>();

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

    // Capture all executed test class names and pass/fail counts
    @AfterMethod
    public void tearDown(ITestResult result) {

        String className = result.getTestClass().getRealClass().getSimpleName();

        // Add to executed classes
        executedTestClasses.add(className);

        // Update pass/fail count
        Map<String, Integer> classResult = testResults.getOrDefault(className, new HashMap<>());
        classResult.put("PASS", classResult.getOrDefault("PASS", 0));
        classResult.put("FAIL", classResult.getOrDefault("FAIL", 0));

        if (result.getStatus() == ITestResult.SUCCESS) {
            classResult.put("PASS", classResult.get("PASS") + 1);
        } else {
            classResult.put("FAIL", classResult.get("FAIL") + 1);
        }

        testResults.put(className, classResult);

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

        // Show all executed test classes
        htmlBody.append("<p><b>Executed Test Classes:</b> ")
                .append(String.join(", ", executedTestClasses))
                .append("</p>");

        htmlBody.append("<p>Please find the attached <b>Word report</b> and <b>Excel report</b> for full execution logs and results.</p>");
        htmlBody.append("<br>");
        htmlBody.append("<p>Regards,<br>Automation Bot</p>");
        htmlBody.append("</body></html>");

        // Get current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        // Get current system username
        String userName = System.getProperty("user.name");

        // Build email subject dynamically
        String emailSubject = "Automation Test Report - " + formattedDateTime + " - Run by " + userName;

        // Generate Excel report via ExcelReportUtil
        File tempExcel = ExcelReportUtil.generateExcelReport();
        String excelPath = tempExcel != null ? tempExcel.getAbsolutePath() : null;



        String[] recipients = {
                "gpasindu55@gmail.com",
                "chathurika.senarath@ifs.com",
                "rashmi.sandeepani@ifs.com"
        };

        // Send email with Word and Excel attachment
        OutlookEmailUtil.sendEmailWithTempWord(
                recipients,
                emailSubject,
                htmlBody.toString(),
                fullConsole,
                excelPath // Attach Excel file
        );
    }
}
