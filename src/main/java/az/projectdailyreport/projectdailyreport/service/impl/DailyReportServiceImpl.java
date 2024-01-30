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
import az.projectdailyreport.projectdailyreport.repository.UserRepository;
import az.projectdailyreport.projectdailyreport.service.DailyReportService;
import az.projectdailyreport.projectdailyreport.service.ProjectService;
import az.projectdailyreport.projectdailyreport.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DailyReportServiceImpl implements DailyReportService {


    private final DailyReportRepository dailyReportRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ProjectService projectService;
    LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
    LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);


    @Override
    public DailyReportDTO createDailyReport(DailyReportRequest dailyReportRequest, User user) {
        Project project = projectService.getProjectById(dailyReportRequest.getProjectId());
        if (dailyReportRepository.existsByProjectAndUserAndLocalDateTimeBetween(
                project, user, startOfDay, endOfDay)) {
            throw new DuplicateReportException("Aynı projeye ve kullanıcıya ait rapor zaten mevcut.");
        }

        // DailyReport'ı oluştur ve kaydet
        DailyReport dailyReport = new DailyReport();
        dailyReport.setFirstName(user.getFirstName());
        dailyReport.setLastName(user.getLastName());
//        dailyReport.setLocalDateTime(LocalDateTime.now());
        LocalDateTime utcDateTime = LocalDateTime.now(ZoneOffset.UTC);
        dailyReport.setLocalDateTime(utcDateTime);
        dailyReport.setReportText(dailyReportRequest.getReportText());
        dailyReport.setUser(user);
        dailyReport.setProject(project);

        DailyReport savedReport = dailyReportRepository.save(dailyReport);

        ModelMapper modelMapper = new ModelMapper();


        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);

        // DailyReportDTO'ya dönüştürme işlemi
        DailyReportDTO dailyReportDTO = modelMapper.map(savedReport, DailyReportDTO.class);




        return dailyReportDTO;
    }

    @Override
    public DailyReportDTO updateDailyReport(Long reportId, DailyReportUpdate updatedReportText) {
        // Belirli bir günlük raporunu id ile bul
        DailyReport existingReport = dailyReportRepository.findById(reportId)
                .orElseThrow(() -> new ReportNotFoundException("Daily Report not found with id: " + reportId));

        // Günün başlangıcı ve bitişi
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);

        // Eğer raporun kaydedildiği gün içinde bir güncelleme yapılmışsa hata fırlat
        if (!existingReport.getLocalDateTime().isAfter(startOfDay) ||
                !existingReport.getLocalDateTime().isBefore(endOfDay)) {
            throw new DailyReportUpdateException("Günlük raporu sadece kaydedildiği gün içinde güncellenebilir.");
        }

        // Sadece reportText alanını güncelle
        existingReport.setReportText(updatedReportText.getReportText());

        ModelMapper modelMapper =new ModelMapper();
        // Güncellenmiş raporu kaydet
        DailyReport updatedReport = dailyReportRepository.save(existingReport);

        // DTO'ya dönüştürme işlemi
        return modelMapper.map(updatedReport, DailyReportDTO.class);
    }

    public List<DailyReportUser> getAllDailyReportsForUser(Long id) {
        List<DailyReport> dailyReports = dailyReportRepository.findByUser_Id(id);
        return mapToUserDTOList(dailyReports);
    }

    public List<DailyReportAdmin> getAllDailyReportsForAdmin() {
        List<DailyReport> dailyReports = dailyReportRepository.findAll();
        return mapToAdminDTOList(dailyReports);
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
                .reportText(dailyReport.getReportText())  // Raporu gizle, isteğe göre farklı bir değer de atanabilir
                .project(mapProjectToDTO(dailyReport.getProject()))
                // Diğer alanlar
                .build();
    }

    private DailyReportAdmin mapToAdminDTO(DailyReport dailyReport) {
        return DailyReportAdmin.builder()
                .id(dailyReport.getId())
                .userId(dailyReport.getUser().getId())
                .firstName(dailyReport.getFirstName())
                .lastName(dailyReport.getLastName())
                .localDateTime(dailyReport.getLocalDateTime())
                .reportText(dailyReport.getReportText())
                .project(mapProjectToDTO(dailyReport.getProject()))
                // Diğer alanlar
                .build();
    }
    public ProjectDTO mapProjectToDTO(Project project) {
        return ProjectDTO.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                // Diğer alanlar
                .build();
    }

    @Override
    public List<DailyReportAdmin> getFilteredDailyReportsForAdmin(List<Long> userIds, LocalDate startDate, LocalDate endDate, List<Long> projectIds, Pageable pageable) {
        List<DailyReport> filteredReports = dailyReportRepository.findByUserIdInAndLocalDateTimeBetweenAndProjectIdIn(userIds, startDate, endDate, projectIds, pageable);
        return mapToAdminDTOList(filteredReports);
    }

    @Override
    public List<DailyReportUser> getUserReportsBetweenDates(Long userId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        List<DailyReport> filteredReports = dailyReportRepository.findUserReportsBetweenDates(userId, startDate, endDate, pageable);
    return mapToUserDTOList(filteredReports);
    }




}



