package az.projectdailyreport.projectdailyreport.dto.team;

import az.projectdailyreport.projectdailyreport.dto.RoleDTO;
import az.projectdailyreport.projectdailyreport.model.Status;
import lombok.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class TeamUserDto {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Status status;
    private RoleDTO role;
}
