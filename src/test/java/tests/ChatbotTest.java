package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import webpages.Chatbot;

import java.util.Arrays;
import java.util.List;

public class ChatbotTest extends BaseTest {

    @Test
    public void runChatbotTest() {
        Chatbot chatbot = new Chatbot(driver);

        // Questions to ask
        List<String> questions = Arrays.asList(
                "What can you do?",
                "Tell me a random tip.",
                "Who created you?"
        );

        // Interact with chatbot and capture replies
        List<String> replies = chatbot.interactWithChatbot(questions);

        System.out.println("=== Captured Chatbot Conversation ===");
        for (int i = 0; i < questions.size(); i++) {
            System.out.println("Q: " + questions.get(i));
            System.out.println("A: " + (i < replies.size() ? replies.get(i) : "No reply"));
            System.out.println();
        }
    }
}
