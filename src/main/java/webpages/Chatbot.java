package webpages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Chatbot {

    private WebDriver driver;
    private WebDriverWait wait;

    public Chatbot(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public List<String> interactWithChatbot(List<String> questions) {
        List<String> answers = new ArrayList<>();

        try {
            // =================== Switch to chatbot iframe ===================
            WebElement chatbotIframe = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("iframe.drift-frame-controller")
            ));
            driver.switchTo().frame(chatbotIframe);
            System.out.println("✅ Switched to chatbot iframe");

            // =================== Click chatbot avatar ===================
            WebElement avatar = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("div.drift-widget-avatar.circle.drift-controller-icon--avatar-avatar")
            ));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", avatar);
            Thread.sleep(500);
            avatar.click();
            System.out.println("✅ Chatbot avatar clicked");

            // =================== Ask questions ===================
            for (String question : questions) {
                // Wait until textarea is visible
                WebElement inputBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("textarea#drift-widget-composer-input")
                ));

                // Scroll and click to focus
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", inputBox);
                Thread.sleep(300);
                inputBox.click();
                inputBox.clear();
                inputBox.sendKeys(question);
                System.out.println("✅ Question typed: " + question);

                // Click send button
                WebElement sendBtn = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("button.drift-widget-composer-send-button")
                ));
                sendBtn.click();
                System.out.println("✅ Question sent");

                // Wait for bot reply
                WebElement lastReply = wait.until(driver1 -> {
                    List<WebElement> messages = driver1.findElements(
                            By.cssSelector("main.drift-widget-chat-layout div.drift-widget-message-bubble--from-bot")
                    );
                    if (!messages.isEmpty()) {
                        return messages.get(messages.size() - 1);
                    }
                    return null;
                });

                String botReply = lastReply.getText();
                answers.add(botReply);
                System.out.println("💬 Chatbot reply: " + botReply);

                Thread.sleep(1000); // small pause
            }

            driver.switchTo().defaultContent(); // back to main page
            System.out.println("✅ Switched back to main page");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error during chatbot interaction");
        }

        return answers;
    }
}
