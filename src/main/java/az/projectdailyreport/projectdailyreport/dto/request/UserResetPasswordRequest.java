package az.projectdailyreport.projectdailyreport.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResetPasswordRequest {

    private String email;
    private String otp;
    private String newPassword;
    private String confirmNewPassword;
}
