package az.projectdailyreport.projectdailyreport.repository;

import az.projectdailyreport.projectdailyreport.dto.dailyreport.DailyReportUser;
import az.projectdailyreport.projectdailyreport.model.DailyReport;
import az.projectdailyreport.projectdailyreport.model.Project;
import az.projectdailyreport.projectdailyreport.model.User;
import az.projectdailyreport.projectdailyreport.dto.project.ProjectDTO;
import org.checkerframework.common.util.report.qual.ReportCreation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ReportCreation
public interface DailyReportRepository  extends JpaRepository<DailyReport,Long> {

    Optional<DailyReport> findByProjectAndUserAndLocalDateTimeBetween(Project project, User user, LocalDateTime start, LocalDateTime end);

    @Query("SELECT dr FROM DailyReport dr " +
            "WHERE (:userIds IS NULL OR (:userIds IS NOT NULL AND dr.user.id IN (:userIds))) " +
            "" +
            "" +
            "" +
//            "AND ((:startDate IS NOT NULL AND :endDate IS NOT NULL AND DATE(dr.localDateTime) BETWEEN :startDate AND :endDate) " +
//            "OR (:startDate IS NOT NULL AND :endDate IS NULL AND DATE(dr.localDateTime) >= :startDate) " +
//            "OR (:startDate IS NULL AND :endDate IS  NULL AND DATE(dr.localDateTime) <= :endDate)) "

            "AND (((:startDate IS NULL OR DATE(dr.localDateTime) >= :startDate)" +
            " AND (:endDate IS NULL OR DATE(dr.localDateTime) <= :startDate))" +
            "   OR DATE(dr.localDateTime) BETWEEN :startDate AND :endDate ) " +


            "AND (:projectIds IS NULL OR (:projectIds IS NOT NULL AND dr.project.id IN (:projectIds)))")
    Page<DailyReport> findByUserIdInAndLocalDateTimeBetweenAndProjectIdIn(
            @Param("userIds") List<Long> userIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("projectIds") List<Long> projectIds,
            Pageable pageable
    );


    boolean existsByProjectAndUserAndLocalDateTimeBetween(Project project, User user, LocalDateTime start, LocalDateTime end);

    List<DailyReport> findByUser_Id(Long userId);
    @Query("SELECT dr FROM DailyReport dr " +
            "WHERE (:userId IS NULL OR dr.user.id = :userId) " +
            "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
            "(:startDate IS NULL AND :endDate IS NOT NULL AND DATE(dr.localDateTime) <= :endDate) OR " +
            "(:startDate IS NOT NULL AND :endDate IS NULL AND DATE(dr.localDateTime) >= :startDate) OR " +
            "(DATE(dr.localDateTime) BETWEEN :startDate AND :endDate))")
    List<DailyReport> findUserReportsBetweenDates(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );


}

