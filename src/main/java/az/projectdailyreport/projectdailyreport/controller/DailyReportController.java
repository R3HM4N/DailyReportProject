package az.projectdailyreport.projectdailyreport.controller;

import az.projectdailyreport.projectdailyreport.dto.DailyReportDTO;
import az.projectdailyreport.projectdailyreport.dto.request.DailyReportRequest;
import az.projectdailyreport.projectdailyreport.model.DailyReport;
import az.projectdailyreport.projectdailyreport.model.User;
import az.projectdailyreport.projectdailyreport.repository.UserRepository;
import az.projectdailyreport.projectdailyreport.service.DailyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class DailyReportController {


    private final DailyReportService dailyReportService;
    private final UserRepository userRepository;
    long a =1;
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
}
