package az.projectdailyreport.projectdailyreport.service.impl;

import az.projectdailyreport.projectdailyreport.model.DailyReport;
import az.projectdailyreport.projectdailyreport.repository.DailyReportRepository;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.servlet.ServletOutputStream;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ExcelExportService {

    private final DailyReportRepository dailyReportRepository;

    public void generateDailyReportExcel(
            HttpServletResponse httpServletResponse,
            List<Long> userIds,
            LocalDate startDate,
            LocalDate endDate,
            List<Long> projectIds,
            Pageable pageable) throws IOException {

        Page<DailyReport> reports = dailyReportRepository.findByUserIdInAndLocalDateTimeBetweenAndProjectIdIn(
                userIds,
                startDate,
                endDate,
                projectIds,
                pageable
        );
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet sheet = hssfWorkbook.createSheet("Product with Variation Info");
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("Daily Report ID");
        row.createCell(1).setCellValue("UserId");
        row.createCell(2).setCellValue("User FirstName");
        row.createCell(3).setCellValue("User LastName");
        row.createCell(4).setCellValue("LocalDate");
        row.createCell(5).setCellValue("DailyReport Description");
        row.createCell(6).setCellValue("ProjectName");

        int dataRowIndex = 1;

        for (DailyReport report : reports) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(report.getId());
            dataRow.createCell(1).setCellValue(report.getUser().getId());
            dataRow.createCell(2).setCellValue(report.getFirstName());
            dataRow.createCell(3).setCellValue(report.getLastName());
            dataRow.createCell(4).setCellValue(report.getLocalDateTime());
            dataRow.createCell(5).setCellValue(report.getReportText());
            dataRow.createCell(6).setCellValue(report.getProject().getProjectName());

            dataRowIndex++;
        }

        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        hssfWorkbook.write(outputStream);
        hssfWorkbook.close();
        outputStream.close();
    }
}
