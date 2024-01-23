package az.projectdailyreport.projectdailyreport.dto.request;

import az.projectdailyreport.projectdailyreport.model.Project;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;
@Data
@RequiredArgsConstructor
public class DailyReportRequest {

    private String reportText;
    private Long projectId;

}
