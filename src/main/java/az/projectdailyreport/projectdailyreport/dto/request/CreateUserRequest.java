package az.projectdailyreport.projectdailyreport.dto.request;
import az.projectdailyreport.projectdailyreport.model.Role;
import az.projectdailyreport.projectdailyreport.model.Team;
import az.projectdailyreport.projectdailyreport.validation.CrocusoftEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class CreateUserRequest {


    @NotBlank(message = "FirstName is required")
    private String firstName;

    @NotBlank(message = "Lastname is required")
    private String lastName;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$",
            message = "Invalid Password pattern. Password must contain 8 to 20 characters at least one digit, lower, upper case ."
    )
    private String password;

    @NotNull(message = "Rol belirtilmelidir")
    private Role role;


    @CrocusoftEmail
    @Builder.Default
    @Email(message = "Geçerli bir e-posta adresi değil")
    private String mail = null;
    private Long teamId;

}


