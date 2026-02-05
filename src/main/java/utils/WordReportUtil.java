package utils;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WordReportUtil {

    /**
     * Generates a Word (.docx) file with the given content.
     * @param fileName Full path including the file name (e.g., "C:\\Users\\cogalk\\Documents\\Report.docx")
     * @param content  Text content to put into the Word file
     * @return File object representing the created Word file, or null if failed
     */
    public static File generateWord(String fileName, String content) {
        try (XWPFDocument document = new XWPFDocument()) {
            String[] lines = content.split("C:\\Users\\cogalk\\OneDrive - IFS\\Desktop\\automated report\\Report.docx");
            for (String line : lines) {
                XWPFParagraph paragraph = document.createParagraph();
                paragraph.createRun().setText(line);
            }

            File file = new File(fileName);

            // Ensure parent directory exists
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
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
