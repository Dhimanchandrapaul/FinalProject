package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelReporter {

    private final XSSFWorkbook workbook;
    private final Sheet        sheet;
    private final String       filePath;
    private       int          rowNum = 1;

    public ExcelReporter(String filePath) {
        this.filePath = filePath;
        this.workbook = new XSSFWorkbook();
        this.sheet    = workbook.createSheet("Test Results");

        String[] headers = {
                "S.No", "Test Case ID", "Test Case Name",
                "Description", "Expected Result", "Actual Result", "Status"
        };

        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }
    }

    public void logResult(int sno, String tcId, String tcName, String description,
                          String expected, String actual, String status) {

        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(sno);
        row.createCell(1).setCellValue(tcId);
        row.createCell(2).setCellValue(tcName);
        row.createCell(3).setCellValue(description);
        row.createCell(4).setCellValue(expected);
        row.createCell(5).setCellValue(actual);
        row.createCell(6).setCellValue(status);
    }

    public void save() {
        try {
            java.io.File file = new java.io.File(filePath);
            file.getParentFile().mkdirs();   // ← creates folder if missing

            if (file.exists() && !file.canWrite()) {
                System.err.println("ERROR: Excel file is open. Close it first!");
                return;
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
                System.out.println("Excel report saved: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Failed to save Excel: " + e.getMessage());
            System.err.println("Make sure the file is NOT open in Excel!");
        } finally {
            try { workbook.close(); } catch (IOException ignored) {}
        }
    }
}