package utils;

import java.io.File;
import java.io.IOException;

public class OutlookEmailUtil {

    /**
     * Sends a simple HTML email via Outlook.
     */
    public static void sendEmail(String[] to, String subject, String htmlBody) {

        String recipients = String.join(";", to);

        String command = "powershell.exe -Command " +
                "\"$Outlook = New-Object -ComObject Outlook.Application; " +
                "$Mail = $Outlook.CreateItem(0); " +
                "$Mail.To = '" + recipients + "'; " +
                "$Mail.Subject = '" + subject + "'; " +
                "$Mail.HTMLBody = '" + htmlBody + "'; " +
                "$Mail.Send()\"";

        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            System.out.println("✅ Email sent via Outlook to: " + recipients);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("❌ Failed to send email via Outlook");
        }
    }

    /**
     * Sends an email with a Word file attachment.
     * This method will generate the Word file from console output and attach it.
     */
    public static void sendEmailWithWordAttachment(String[] to, String subject, String htmlBody, String consoleOutput, String wordFileName) {

        // Step 1: Generate Word file from console output
        File wordFile = WordReportUtil.generateWord(wordFileName, consoleOutput);
        if (wordFile == null) {
            System.out.println("❌ Cannot send email: Word file generation failed.");
            return;
        }

        // Step 2: Prepare recipients
        String recipients = String.join(";", to);

        // Step 3: PowerShell command to send email with attachment
        String command = "powershell.exe -Command " +
                "\"$Outlook = New-Object -ComObject Outlook.Application; " +
                "$Mail = $Outlook.CreateItem(0); " +
                "$Mail.To = '" + recipients + "'; " +
                "$Mail.Subject = '" + subject + "'; " +
                "$Mail.HTMLBody = '" + htmlBody + "'; " +
                "$Mail.Attachments.Add('" + wordFile.getAbsolutePath().replace("\\", "\\\\") + "'); " +
                "$Mail.Send()\"";

        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            System.out.println("✅ Email with Word attachment sent to: " + recipients);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("❌ Failed to send email with Word attachment");
        }
    }
}
