package az.projectdailyreport.projectdailyreport.service.impl;

import az.projectdailyreport.projectdailyreport.dto.request.DailyReportRequest;
import az.projectdailyreport.projectdailyreport.exception.DuplicateReportException;
import az.projectdailyreport.projectdailyreport.model.DailyReport;
import az.projectdailyreport.projectdailyreport.model.Project;
import az.projectdailyreport.projectdailyreport.model.User;
import az.projectdailyreport.projectdailyreport.repository.DailyReportRepository;
import az.projectdailyreport.projectdailyreport.service.DailyReportService;
import az.projectdailyreport.projectdailyreport.service.ProjectService;
import az.projectdailyreport.projectdailyreport.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;


@Service
@RequiredArgsConstructor
public class DailyReportServiceImpl implements DailyReportService {


    private final DailyReportRepository dailyReportRepository;
    private final UserService userService;
    private final ProjectService projectService;
    LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
    LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);


    @Override
    public DailyReport createDailyReport(DailyReportRequest dailyReportRequest, User user) {
        Project project = projectService.getProjectById(dailyReportRequest.getProjectId());
        if (dailyReportRepository.existsByProjectAndUserAndLocalDateTimeBetween(
                project, user, startOfDay, endOfDay)) {
            throw new DuplicateReportException("Aynı projeye ve kullanıcıya ait rapor zaten mevcut.");
        }
        DailyReport dailyReport = new DailyReport();
        dailyReport.setFirstName(user.getFirstName());
        dailyReport.setLastName(user.getLastName());
        dailyReport.setLocalDateTime(LocalDateTime.now());
        dailyReport.setReportText(dailyReportRequest.getReportText());
        dailyReport.setUser(user);
        dailyReport.setProject(project);

        return dailyReportRepository.save(dailyReport);
}}
