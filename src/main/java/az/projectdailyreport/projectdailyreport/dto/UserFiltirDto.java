package az.projectdailyreport.projectdailyreport.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class UserFiltirDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String status;
    private List<Long> teamId;
    private List<Long> projectIds;
}
