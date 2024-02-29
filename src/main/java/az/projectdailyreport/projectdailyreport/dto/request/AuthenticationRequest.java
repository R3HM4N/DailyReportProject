package az.projectdailyreport.projectdailyreport.dto.request;

import az.projectdailyreport.projectdailyreport.validation.CrocusoftEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
//    @CrocusoftEmail
    @Email(message = "Geçerli bir e-posta adresi değil")
    private String mail;
    @NotBlank(message = "Password is required")
    private String password;

}
