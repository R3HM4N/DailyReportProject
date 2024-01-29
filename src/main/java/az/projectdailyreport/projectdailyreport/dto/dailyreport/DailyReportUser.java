package az.projectdailyreport.projectdailyreport.dto.dailyreport;

import az.projectdailyreport.projectdailyreport.dto.UserDTO;
import az.projectdailyreport.projectdailyreport.dto.project.ProjectDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyReportUser {

    private Long id;
    private Long userId;
    private LocalDateTime localDateTime;
    private String reportText;
    private ProjectDTO project;
}
