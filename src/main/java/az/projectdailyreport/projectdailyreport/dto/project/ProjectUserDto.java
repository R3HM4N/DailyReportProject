package az.projectdailyreport.projectdailyreport.dto.project;

import az.projectdailyreport.projectdailyreport.dto.team.TeamDTO;
import az.projectdailyreport.projectdailyreport.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private Status status;
    private String mail;
    private TeamDTO team;
}
