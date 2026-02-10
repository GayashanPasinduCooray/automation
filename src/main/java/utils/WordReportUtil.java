package utils;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WordReportUtil {

    /**
     * Generates a Word (.docx) file with the given content.
     *
     * @param filePath Full path of the Word file (can be temp file)
     * @param content  Text content to put into the Word file
     * @return File object representing the created Word file, or null if failed
     */
    public static File generateWord(String filePath, String content) {

        try (XWPFDocument document = new XWPFDocument()) {

            // Split console output by lines
            String[] lines = content.split("\\r?\\n");

            for (String line : lines) {
                XWPFParagraph paragraph = document.createParagraph();
                paragraph.createRun().setText(line); // preserves emojis
            }

            File file = new File(filePath);

            // Ensure parent directory exists
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            try (FileOutputStream out = new FileOutputStream(file)) {
                document.write(out);
            }

            System.out.println("✅ Word file generated: " + file.getAbsolutePath());
            return file;

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Failed to generate Word file");
            return null;
        }
    }
}
