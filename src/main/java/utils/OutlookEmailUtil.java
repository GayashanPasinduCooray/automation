package utils;

import java.io.File;
import java.io.IOException;

public class OutlookEmailUtil {

    // Updated to include optional Excel attachment
    public static void sendEmailWithTempWord(String[] to, String subject, String htmlBody, String consoleOutput, String excelFilePath) {

        try {
            // Create temp Word file (will auto-delete on exit)
            File tempWord = File.createTempFile("Automation_Console_Report_", ".docx");
            tempWord.deleteOnExit();

            // Generate Word content
            utils.WordReportUtil.generateWord(tempWord.getAbsolutePath(), consoleOutput);

            // Prepare recipients
            String recipients = String.join(";", to);

            // PowerShell command to send email with both attachments
            String command = "powershell.exe -Command " +
                    "\"$Outlook = New-Object -ComObject Outlook.Application; " +
                    "$Mail = $Outlook.CreateItem(0); " +
                    "$Mail.To = '" + recipients + "'; " +
                    "$Mail.Subject = '" + subject + "'; " +
                    "$Mail.HTMLBody = '" + htmlBody + "'; " +
                    "$Mail.Attachments.Add('" + tempWord.getAbsolutePath().replace("\\", "\\\\") + "'); ";

            if (excelFilePath != null && !excelFilePath.isEmpty()) {
                command += "$Mail.Attachments.Add('" + excelFilePath.replace("\\", "\\\\") + "'); ";
            }

            command += "$Mail.Send()\"";

            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();

            System.out.println("✅ Email with Word + Excel attachment sent to: " + recipients);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("❌ Failed to send email with attachments");
        }
    }
}
