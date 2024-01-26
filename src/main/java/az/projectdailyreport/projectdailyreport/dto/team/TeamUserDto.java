package az.projectdailyreport.projectdailyreport.dto.team;

import az.projectdailyreport.projectdailyreport.dto.RoleDTO;
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
    private RoleDTO role;
}
