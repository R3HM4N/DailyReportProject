package az.projectdailyreport.projectdailyreport.service.impl;
import az.projectdailyreport.projectdailyreport.dto.dailyreport.DailyReportAdmin;
import az.projectdailyreport.projectdailyreport.dto.dailyreport.DailyReportDTO;
import az.projectdailyreport.projectdailyreport.dto.dailyreport.DailyReportUpdate;
import az.projectdailyreport.projectdailyreport.dto.dailyreport.DailyReportUser;
import az.projectdailyreport.projectdailyreport.dto.project.ProjectDTO;
import az.projectdailyreport.projectdailyreport.dto.UserDTO;
import az.projectdailyreport.projectdailyreport.dto.request.DailyReportRequest;
import az.projectdailyreport.projectdailyreport.exception.DailyReportUpdateException;
import az.projectdailyreport.projectdailyreport.exception.DuplicateReportException;
import az.projectdailyreport.projectdailyreport.exception.ReportNotFoundException;
import az.projectdailyreport.projectdailyreport.model.DailyReport;
import az.projectdailyreport.projectdailyreport.model.Project;
import az.projectdailyreport.projectdailyreport.model.User;
import az.projectdailyreport.projectdailyreport.repository.DailyReportRepository;
import az.projectdailyreport.projectdailyreport.service.DailyReportService;
import az.projectdailyreport.projectdailyreport.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class DailyReportServiceImpl implements DailyReportService {


    private final DailyReportRepository dailyReportRepository;

    private static final Logger logger = LoggerFactory.getLogger(DailyReportServiceImpl.class);


    private final ProjectService projectService;
    LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
    LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);

    private String parseHtmlAndExtractText(String html) {
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p");
        StringBuilder textBuilder = new StringBuilder();
        for (Element paragraph : paragraphs) {
            textBuilder.append(paragraph.text());
        }
        return textBuilder.toString();
    }

    @Override
    public DailyReportDTO createDailyReport(DailyReportRequest dailyReportRequest, User user) {
        Project project = projectService.getProjectById (dailyReportRequest.getProjectId());
        if (dailyReportRepository.existsByProjectAndUserAndLocalDateTimeBetween(
                project, user, startOfDay, endOfDay)) {
            throw new DuplicateReportException("Aynı projeye ve kullanıcıya ait rapor zaten mevcut.");
        }

        DailyReport dailyReport = new DailyReport();
        dailyReport.setFirstName(user.getFirstName());
        dailyReport.setLastName(user.getLastName());
        LocalDateTime utcDateTime = LocalDateTime.now(ZoneOffset.UTC);
        dailyReport.setLocalDateTime(utcDateTime);
        dailyReport.setReportText(dailyReportRequest.getReportText());
        dailyReport.setUser(user);
        dailyReport.setProject(project);

        DailyReport savedReport = dailyReportRepository.save(dailyReport);
        ModelMapper modelMapper = new ModelMapper();

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);
        log.info("Report created");

        return   modelMapper.map(savedReport, DailyReportDTO.class);
    }

    @Override
    public DailyReportDTO updateDailyReport(Long reportId, DailyReportUpdate updatedReportText) {
        DailyReport existingReport = dailyReportRepository.findById(reportId)
                .orElseThrow(() -> new ReportNotFoundException("Daily Report not found with id: " + reportId));

        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);

        if (!existingReport.getLocalDateTime().isAfter(startOfDay) ||
                !existingReport.getLocalDateTime().isBefore(endOfDay)) {
            throw new DailyReportUpdateException("Günlük raporu sadece kaydedildiği gün içinde güncellenebilir.");
        }

        existingReport.setReportText(updatedReportText.getReportText());

        ModelMapper modelMapper =new ModelMapper();
        DailyReport updatedReport = dailyReportRepository.save(existingReport);

        log.info("report updated");

        return modelMapper.map(updatedReport, DailyReportDTO.class);
    }


    @Override
    public DailyReportAdmin getById(Long reportId) {
        DailyReport report = dailyReportRepository.findById(reportId)
                .orElseThrow(() -> new ReportNotFoundException("Daily Report not found with id: " + reportId));

        return mapToAdminDTO(report);
    }

    private List<DailyReportUser> mapToUserDTOList(List<DailyReport> dailyReports) {


        return dailyReports.stream()
                .map(this::mapToUserDTO)
                .collect(Collectors.toList());

    }

    private List<DailyReportAdmin> mapToAdminDTOList(List<DailyReport> dailyReports) {
        return dailyReports.stream()
                .map(this::mapToAdminDTO)
                .collect(Collectors.toList());
    }

    private DailyReportUser mapToUserDTO(DailyReport dailyReport) {
        return DailyReportUser.builder()
                .id(dailyReport.getId())
                .userId(dailyReport.getUser().getId())
                .localDateTime(dailyReport.getLocalDateTime())
                .reportText(parseHtmlAndExtractText(dailyReport.getReportText()))
                .project(mapProjectToDTO(dailyReport.getProject()))
                .build();
    }

    private DailyReportAdmin mapToAdminDTO(DailyReport dailyReport) {
        return DailyReportAdmin.builder()
                .id(dailyReport.getId())
                .userId(dailyReport.getUser().getId())
                .firstName(dailyReport.getFirstName())
                .lastName(dailyReport.getLastName())
                .localDateTime(dailyReport.getLocalDateTime())
                .reportText(parseHtmlAndExtractText(dailyReport.getReportText()))
                .project(mapProjectToDTO(dailyReport.getProject()))
                .build();
    }
    public ProjectDTO mapProjectToDTO(Project project) {
        return ProjectDTO.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .build();
    }

    @Override
    public Page<DailyReportAdmin> getFilteredDailyReportsForAdmin(
            List<Long> userIds, LocalDate startDate, LocalDate endDate,
            List<Long> projectIds, Pageable pageable) {

        if (userIds==null  && startDate==null && endDate==null && projectIds==null){
            Page<DailyReport> getall = dailyReportRepository.findAll(pageable);
            return getall.map(this::mapToAdminDTO);
        }else {
        Page<DailyReport> filteredReports = dailyReportRepository.findByUserIdInAndLocalDateTimeBetweenAndProjectIdIn(
                userIds, startDate, endDate, projectIds, pageable);
        return filteredReports.map(this::mapToAdminDTO);}
    }
    @Override
    public Page<DailyReportUser> getUserReportsBetweenDates(Long userId, LocalDate startDate, LocalDate endDate,List<Long> projectIds, Pageable pageable) {


            Page<DailyReport> filteredReports = dailyReportRepository.findUserReportsBetweenDates(userId, startDate, endDate, projectIds, pageable);
            return filteredReports.map(this::mapToUserDTO);

    }
}