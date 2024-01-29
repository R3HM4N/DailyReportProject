package az.projectdailyreport.projectdailyreport.controller;

import az.projectdailyreport.projectdailyreport.dto.dailyreport.DailyReportAdmin;
import az.projectdailyreport.projectdailyreport.dto.dailyreport.DailyReportDTO;
import az.projectdailyreport.projectdailyreport.dto.dailyreport.DailyReportUpdate;
import az.projectdailyreport.projectdailyreport.dto.dailyreport.DailyReportUser;
import az.projectdailyreport.projectdailyreport.dto.request.DailyReportRequest;
import az.projectdailyreport.projectdailyreport.model.DailyReport;
import az.projectdailyreport.projectdailyreport.model.User;
import az.projectdailyreport.projectdailyreport.repository.UserRepository;
import az.projectdailyreport.projectdailyreport.service.DailyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    long a =1;
    long b = 2;
    @PostMapping("/reports")
    public ResponseEntity<DailyReportDTO> createDailyReport(@RequestBody DailyReportRequest reportRequest) {
        Optional<User> userOptional = userRepository.findById(a);

        //        User signedInUser = authenticationService.getSignedInUser();

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
    @GetMapping("/user")
    public ResponseEntity<List<DailyReportUser>> getAllDailyReportsForUser() {

        Optional<User> userOptional = userRepository.findById(b);



        List<DailyReportUser> dailyReportsForUser = dailyReportService.getAllDailyReportsForUser(userOptional.get().getId());
        return ResponseEntity.ok(dailyReportsForUser);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<DailyReportAdmin>> getAllDailyReportsForAdmin() {
        List<DailyReportAdmin> dailyReportsForAdmin = dailyReportService.getAllDailyReportsForAdmin();
        return ResponseEntity.ok(dailyReportsForAdmin);
    }
    @GetMapping("/admin/filtir")
    public ResponseEntity<List<DailyReportAdmin>> getFilteredDailyReportsForAdmin(
            @RequestParam(required = false) List<String> firstNames,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate,
            @RequestParam(name = "pageNumber", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "ASC") String sortOrder    )

    {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortOrder), sortBy);

        List<DailyReportAdmin> filteredReports = dailyReportService.getFilteredDailyReportsForAdmin(firstNames, startDate, endDate, pageable);
        return ResponseEntity.ok(filteredReports);
    }

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

        Optional<User> userOptional = userRepository.findById(a);

        List<DailyReportUser> reports = dailyReportService.getUserReportsBetweenDates(userOptional.get().getId(), startDate, endDate, pageable);
        return ResponseEntity.ok(reports);
    }

}


