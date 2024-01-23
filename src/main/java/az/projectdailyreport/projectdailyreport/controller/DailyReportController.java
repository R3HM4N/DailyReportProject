package az.projectdailyreport.projectdailyreport.controller;

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
    @PostMapping("/reports")//public
    public ResponseEntity<DailyReport> createDailyReport(@RequestBody DailyReportRequest reportRequest) {
//        User signedInUser = authenticationService.getSignedInUser();

        Optional<User> user1 =userRepository.findById(a);
        DailyReport createdReport = dailyReportService.createDailyReport(reportRequest,user1.get());
        return new ResponseEntity<>(createdReport, HttpStatus.CREATED);
    }
}
