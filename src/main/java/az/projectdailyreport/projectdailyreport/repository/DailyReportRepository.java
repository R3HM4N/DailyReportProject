package az.projectdailyreport.projectdailyreport.repository;

import az.projectdailyreport.projectdailyreport.model.DailyReport;
import org.checkerframework.common.util.report.qual.ReportCreation;
import org.springframework.data.jpa.repository.JpaRepository;
@ReportCreation
public interface DailyReportRepository  extends JpaRepository<DailyReport,Long> {
}
