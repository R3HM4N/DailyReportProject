package az.projectdailyreport.projectdailyreport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor

public class DailyReportDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDateTime localDateTime;
    private String reportText;

    public DailyReportDTO(Long id, String firstName, String lastName, LocalDateTime localDateTime, String reportText) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.localDateTime = localDateTime;
        this.reportText = reportText;
    }
}
