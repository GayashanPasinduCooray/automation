package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

public class ExcelReportUtil {

    public static File generateExcelReport() {

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            /* ================= SHEET 1: TEST EXECUTION ================= */

            XSSFSheet executionSheet = workbook.createSheet("Test Execution");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle tableCellStyle = createTableCellStyle(workbook);

            Row execHeader = executionSheet.createRow(0);
            String[] execHeaders =
                    {"Page Class", "Pass Count", "Fail Count", "Total"};

            for (int i = 0; i < execHeaders.length; i++) {
                Cell cell = execHeader.createCell(i);
                cell.setCellValue(execHeaders[i]);
                cell.setCellStyle(headerStyle);
            }

            int execRow = 1;

            for (Map.Entry<String, Map<String, Integer>> entry :
                    ValidationResultTracker.getResults().entrySet()) {

                int pass = entry.getValue().getOrDefault("PASS", 0);
                int fail = entry.getValue().getOrDefault("FAIL", 0);

                Row row = executionSheet.createRow(execRow++);
                createStyledCell(row, 0, entry.getKey(), tableCellStyle);
                createStyledCell(row, 1, pass, tableCellStyle);
                createStyledCell(row, 2, fail, tableCellStyle);
                createStyledCell(row, 3, pass + fail, tableCellStyle);
            }

            autoSize(executionSheet, 4);

            /* ================= SHEET 2: ANALYSIS ================= */

            XSSFSheet analysisSheet = workbook.createSheet("Analysis");

            Row header = analysisSheet.createRow(0);
            String[] analysisHeaders =
                    {"Page Class", "Pass %", "Execution Time (sec)"};

            for (int i = 0; i < analysisHeaders.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(analysisHeaders[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIndex = 1;

            for (String page : ValidationResultTracker.getResults().keySet()) {

                int pass = ValidationResultTracker.getResults()
                        .get(page).getOrDefault("PASS", 0);
                int fail = ValidationResultTracker.getResults()
                        .get(page).getOrDefault("FAIL", 0);

                int total = pass + fail;
                double passPct = total == 0 ? 0 : (pass * 100.0 / total);

                long timeMs = ValidationResultTracker.getExecutionTime()
                        .getOrDefault(page, 0L);

                Row row = analysisSheet.createRow(rowIndex++);
                createStyledCell(row, 0, page, tableCellStyle);
                createStyledCell(row, 1, passPct, tableCellStyle);
                createStyledCell(row, 2, timeMs / 1000.0, tableCellStyle);
            }

            autoSize(analysisSheet, 3);

            /* ================= 🔴🟢 CONDITIONAL FORMATTING ================= */

            SheetConditionalFormatting cf =
                    analysisSheet.getSheetConditionalFormatting();

            ConditionalFormattingRule greenRule =
                    cf.createConditionalFormattingRule(
                            ComparisonOperator.GE, "80");
            PatternFormatting greenFill =
                    greenRule.createPatternFormatting();
            greenFill.setFillForegroundColor(
                    IndexedColors.LIGHT_GREEN.getIndex());
            greenFill.setFillPattern(
                    PatternFormatting.SOLID_FOREGROUND);

            ConditionalFormattingRule redRule =
                    cf.createConditionalFormattingRule(
                            ComparisonOperator.LT, "80");
            PatternFormatting redFill =
                    redRule.createPatternFormatting();
            redFill.setFillForegroundColor(
                    IndexedColors.ROSE.getIndex());
            redFill.setFillPattern(
                    PatternFormatting.SOLID_FOREGROUND);

            CellRangeAddress[] passRange = {
                    new CellRangeAddress(1, rowIndex - 1, 1, 1)
            };

            cf.addConditionalFormatting(passRange, greenRule, redRule);

            /* ================= BAR CHART (FIXED) ================= */

            XSSFDrawing drawing =
                    analysisSheet.createDrawingPatriarch();

            XSSFClientAnchor anchor = new XSSFClientAnchor();
            anchor.setCol1(4);
            anchor.setRow1(1);
            anchor.setCol2(12);
            anchor.setRow2(18);

            XSSFChart chart = drawing.createChart(anchor);
            chart.setTitleText("Pass Percentage by Page");
            chart.setTitleOverlay(false);

            XDDFCategoryAxis xAxis =
                    chart.createCategoryAxis(AxisPosition.BOTTOM);
            xAxis.setTitle("Page Class");

            XDDFValueAxis yAxis =
                    chart.createValueAxis(AxisPosition.LEFT);
            yAxis.setTitle("Pass %");
            yAxis.setMinimum(0);
            yAxis.setMaximum(100);

            XDDFDataSource<String> categories =
                    XDDFDataSourcesFactory.fromStringCellRange(
                            analysisSheet,
                            new CellRangeAddress(1, rowIndex - 1, 0, 0));

            XDDFNumericalDataSource<Double> values =
                    XDDFDataSourcesFactory.fromNumericCellRange(
                            analysisSheet,
                            new CellRangeAddress(1, rowIndex - 1, 1, 1));

            XDDFChartData chartData =
                    chart.createData(ChartTypes.BAR, xAxis, yAxis);

            XDDFChartData.Series series =
                    chartData.addSeries(categories, values);
            series.setTitle("Pass %", null); // ⭐ CRITICAL FIX

            ((XDDFBarChartData) chartData)
                    .setBarDirection(BarDirection.COL);

            chart.plot(chartData);

            /* ================= SAVE FILE ================= */

            File file =
                    File.createTempFile(
                            "Automation_Enterprise_Report_", ".xlsx");
            file.deleteOnExit();

            try (FileOutputStream fos =
                         new FileOutputStream(file)) {
                workbook.write(fos);
            }

            System.out.println(
                    "✅ Excel report generated with chart + styles");
            return file;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* ================= STYLES ================= */

    private static CellStyle createHeaderStyle(XSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(
                IndexedColors.VIOLET.getIndex());
        style.setFillPattern(
                FillPatternType.SOLID_FOREGROUND);
        setBorders(style);
        return style;
    }

    private static CellStyle createTableCellStyle(XSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        setBorders(style);
        return style;
    }

    private static void setBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    private static void createStyledCell(
            Row row, int col, Object value, CellStyle style) {

        Cell cell = row.createCell(col);

        if (value instanceof String)
            cell.setCellValue((String) value);
        else if (value instanceof Integer)
            cell.setCellValue((Integer) value);
        else if (value instanceof Double)
            cell.setCellValue((Double) value);

        cell.setCellStyle(style);
    }

    private static void autoSize(Sheet sheet, int cols) {
        for (int i = 0; i < cols; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
