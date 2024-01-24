package az.projectdailyreport.projectdailyreport.service;

import az.projectdailyreport.projectdailyreport.dto.DailyReportDTO;
import az.projectdailyreport.projectdailyreport.dto.request.DailyReportRequest;
import az.projectdailyreport.projectdailyreport.model.DailyReport;
import az.projectdailyreport.projectdailyreport.model.User;

public interface DailyReportService {
     DailyReportDTO createDailyReport(DailyReportRequest dailyReportRequest, User user);
}
