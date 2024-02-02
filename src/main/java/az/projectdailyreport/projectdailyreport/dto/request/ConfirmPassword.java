package az.projectdailyreport.projectdailyreport.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmPassword {
    private String newPassword;
    private String confirmNewPassword;
}
