package az.projectdailyreport.projectdailyreport.repository;

import az.projectdailyreport.projectdailyreport.model.DailyReport;
import az.projectdailyreport.projectdailyreport.model.Project;
import az.projectdailyreport.projectdailyreport.model.User;
import org.checkerframework.common.util.report.qual.ReportCreation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@ReportCreation
public interface DailyReportRepository  extends JpaRepository<DailyReport,Long> {
    boolean existsByUser_IdAndProject_IdAndLocalDateTime(Long userId, Long projectId, LocalDateTime localDateTime);

    Optional<DailyReport> findByProjectAndUserAndLocalDateTimeBetween(Project project, User user, LocalDateTime start, LocalDateTime end);

    boolean existsByProjectAndUserAndLocalDateTimeBetween(Project project, User user, LocalDateTime start, LocalDateTime end);
}

