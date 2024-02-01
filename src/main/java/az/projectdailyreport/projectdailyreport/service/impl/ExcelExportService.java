package az.projectdailyreport.projectdailyreport.service.impl;

import az.projectdailyreport.projectdailyreport.dto.dailyreport.DailyReportAdmin;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service

public class ExcelExportService {

    public void exportToExcel(List<DailyReportAdmin> reports, String filePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("FilteredReports");

            // Header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("First Name");
            headerRow.createCell(2).setCellValue("Last Name");
            headerRow.createCell(3).setCellValue("Created Time");
            headerRow.createCell(4).setCellValue("Daily Report");
            // Diğer sütun başlıklarını ekleyin

            // Verileri yaz
            int rowNum = 1;
            for (DailyReportAdmin report : reports) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(report.getId());
                row.createCell(1).setCellValue(report.getFirstName());
                row.createCell(2).setCellValue(report.getLastName());
                row.createCell(3).setCellValue(report.getLocalDateTime());
                row.createCell(4).setCellValue(report.getReportText());
                // Diğer sütun değerlerini ekleyin
            }

            // Dosyayı kaydet
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
    }
}
