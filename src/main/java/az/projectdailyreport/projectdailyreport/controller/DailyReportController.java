package az.projectdailyreport.projectdailyreport.controller;

import az.projectdailyreport.projectdailyreport.dto.dailyreport.DailyReportAdmin;
import az.projectdailyreport.projectdailyreport.dto.dailyreport.DailyReportDTO;
import az.projectdailyreport.projectdailyreport.dto.dailyreport.DailyReportUpdate;
import az.projectdailyreport.projectdailyreport.dto.dailyreport.DailyReportUser;
import az.projectdailyreport.projectdailyreport.dto.project.ProjectFilterDto;
import az.projectdailyreport.projectdailyreport.dto.request.DailyReportRequest;
import az.projectdailyreport.projectdailyreport.model.DailyReport;
import az.projectdailyreport.projectdailyreport.model.User;
import az.projectdailyreport.projectdailyreport.repository.UserRepository;
import az.projectdailyreport.projectdailyreport.service.DailyReportService;
import az.projectdailyreport.projectdailyreport.service.UserService;
import az.projectdailyreport.projectdailyreport.service.impl.ExcelExportService;
import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class DailyReportController {


    private final DailyReportService dailyReportService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ExcelExportService excelExportService;

    @PostMapping("/reports")
    public ResponseEntity<DailyReportDTO> createDailyReport(@RequestBody DailyReportRequest reportRequest) {

              User signedInUser = userService.getSignedInUser();
        Optional<User> userOptional = userRepository.findById(signedInUser.getId());


        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = userOptional.get();
        DailyReportDTO createdReport = dailyReportService.createDailyReport(reportRequest, user);

        return new ResponseEntity<>(createdReport, HttpStatus.CREATED);
    }

    @PutMapping("/{reportId}")
    public ResponseEntity<DailyReportDTO> updateDailyReport(@PathVariable Long reportId,
                                                            @RequestBody DailyReportUpdate updatedReportText) {

            DailyReportDTO updatedReportDTO = dailyReportService.updateDailyReport(reportId, updatedReportText);
            return new ResponseEntity<>(updatedReportDTO, HttpStatus.OK);

    }
//    @GetMapping("/user")
//    public ResponseEntity<List<DailyReportUser>> getAllDailyReportsForUser() {
//
//        User signedInUser = userService.getSignedInUser();
//        Optional<User> userOptional = userRepository.findById(signedInUser.getId());
//
//
//        List<DailyReportUser> dailyReportsForUser = dailyReportService.getAllDailyReportsForUser(userOptional.get().getId());
//        return ResponseEntity.ok(dailyReportsForUser);
//    }
//
//    @GetMapping("/admin")
//    public ResponseEntity<List<DailyReportAdmin>> getAllDailyReportsForAdmin() {
//        List<DailyReportAdmin> dailyReportsForAdmin = dailyReportService.getAllDailyReportsForAdmin();
//        return ResponseEntity.ok(dailyReportsForAdmin);
//    }

    @GetMapping("/user/reports")
    public ResponseEntity<List<DailyReportUser>> getUserReportsBetweenDates(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(name = "pageNumber", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "ASC") String sortOrder    )

    {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortOrder), sortBy);

        User signedInUser = userService.getSignedInUser();
        Optional<User> userOptional = userRepository.findById(signedInUser.getId());
        List<DailyReportUser> reports = dailyReportService.getUserReportsBetweenDates(userOptional.get().getId(), startDate, endDate, pageable);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/admin/filtir")
    public ResponseEntity<Page<DailyReportAdmin>> getFilteredDailyReportsForAdmin(
            @RequestParam(required = false) List<Long> userIds,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) List<Long> projectIds,
            @RequestParam(name = "pageNumber", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "ASC") String sortOrder) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortOrder), sortBy);

        Page<DailyReportAdmin> filteredReports = dailyReportService.getFilteredDailyReportsForAdmin(userIds, startDate, endDate, projectIds, pageable);
        return ResponseEntity.ok(filteredReports);
    }

    @GetMapping("/{report_id}")
        public ResponseEntity<DailyReportAdmin> getById(@PathVariable Long report_id){
        DailyReportAdmin reportAdmin =dailyReportService.getById(report_id);
        return ResponseEntity.ok(reportAdmin);
    }



    @GetMapping("/export-excel")
    public void exportDailyReportsToExcel(HttpServletResponse response,
                                          @RequestParam(required = false) List<Long> userIds,
                                          @RequestParam(required = false) LocalDate startDate,
                                          @RequestParam(required = false) LocalDate endDate,
                                          @RequestParam(required = false) List<Long> projectIds,
                                          @RequestParam(name = "pageNumber", defaultValue = "0") Integer page,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer size,
                                          @RequestParam(name = "sortBy", defaultValue = "id", required = false) String sortBy,
                                          @RequestParam(name = "sortOrder", defaultValue = "ASC") String sortOrder
                                           ) throws IOException {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortOrder), sortBy);

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=daily-reports.xlsx");

        excelExportService.generateDailyReportExcel(response, userIds, startDate, endDate, projectIds, pageable);
    }
}








