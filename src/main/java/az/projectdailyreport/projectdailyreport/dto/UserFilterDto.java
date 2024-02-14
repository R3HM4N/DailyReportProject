package az.projectdailyreport.projectdailyreport.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class UserFilterDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String status;
    private List<Long> teamId;
    private List<Long> projectIds;
}
