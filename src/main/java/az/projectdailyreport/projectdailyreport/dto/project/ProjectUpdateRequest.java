package az.projectdailyreport.projectdailyreport.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUpdateRequest {
    private String projectName;
    private List<Long> userIdsToAdd;
    private List<Long> userIdsToRemove;
}
