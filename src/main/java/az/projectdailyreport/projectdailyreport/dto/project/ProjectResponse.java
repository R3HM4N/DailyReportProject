package az.projectdailyreport.projectdailyreport.dto.project;

import az.projectdailyreport.projectdailyreport.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
    private Long id;
    private String projectName;
    private Set<UserDTO> users;

}
