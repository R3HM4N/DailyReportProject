package az.projectdailyreport.projectdailyreport.dto.request;

import az.projectdailyreport.projectdailyreport.validation.CrocusoftEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @NotBlank(message = "Username is required")
    @CrocusoftEmail
    @Email(message = "Geçerli bir e-posta adresi değil")
    private String mail;
//    @NotBlank(message = "Password is required")
//    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$",
//            message = "Invalid Password pattern. Password must contain 8 to 20 characters at least one digit, lower, upper case ."
//    )
    private String password;

}
