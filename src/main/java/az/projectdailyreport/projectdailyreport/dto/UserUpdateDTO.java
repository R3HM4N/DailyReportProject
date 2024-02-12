package az.projectdailyreport.projectdailyreport.dto;

import az.projectdailyreport.projectdailyreport.dto.team.TeamDTO;
import az.projectdailyreport.projectdailyreport.model.Role;
import az.projectdailyreport.projectdailyreport.model.Status;
import az.projectdailyreport.projectdailyreport.model.Team;
import az.projectdailyreport.projectdailyreport.validation.CrocusoftEmail;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    private String firstName;
    private String lastName;
    @CrocusoftEmail
    @Email(message = "Not a valid email address")
    private String email;
    private Long roleId;
    private Long teamId;
}
