package az.projectdailyreport.projectdailyreport.service;

import az.projectdailyreport.projectdailyreport.dto.request.DailyReportRequest;
import az.projectdailyreport.projectdailyreport.model.DailyReport;
import az.projectdailyreport.projectdailyreport.model.User;

public interface DailyReportService {
     DailyReport createDailyReport(DailyReportRequest dailyReportRequest, User user);
}
