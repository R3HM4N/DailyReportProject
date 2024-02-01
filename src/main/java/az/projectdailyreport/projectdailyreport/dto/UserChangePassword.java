package az.projectdailyreport.projectdailyreport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePassword {
    private String oldpassword;
    private String newPassword;
    private String newConfirimPassword;
}
