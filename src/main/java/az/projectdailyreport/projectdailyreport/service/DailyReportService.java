package az.projectdailyreport.projectdailyreport.service;

import az.projectdailyreport.projectdailyreport.dto.dailyreport.DailyReportAdmin;
import az.projectdailyreport.projectdailyreport.dto.dailyreport.DailyReportDTO;
import az.projectdailyreport.projectdailyreport.dto.dailyreport.DailyReportUpdate;
import az.projectdailyreport.projectdailyreport.dto.dailyreport.DailyReportUser;
import az.projectdailyreport.projectdailyreport.dto.request.DailyReportRequest;
import az.projectdailyreport.projectdailyreport.model.DailyReport;
import az.projectdailyreport.projectdailyreport.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DailyReportService {
     DailyReportDTO createDailyReport(DailyReportRequest dailyReportRequest, User user);
     List<DailyReportUser> getAllDailyReportsForUser(Long id);
     List<DailyReportAdmin> getAllDailyReportsForAdmin();
     List<DailyReportUser> getUserReportsBetweenDates(Long userId, LocalDate startDate, LocalDate endDate, Pageable pageable);
     Page<DailyReportAdmin> getFilteredDailyReportsForAdmin(
             List<Long> userIds, LocalDate startDate, LocalDate endDate,
             List<Long> projectIds, Pageable pageable);
     DailyReportDTO updateDailyReport(Long reportId, DailyReportUpdate updatedReportText);
}

