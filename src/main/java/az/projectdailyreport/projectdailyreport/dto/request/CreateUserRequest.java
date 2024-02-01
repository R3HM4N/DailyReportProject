package az.projectdailyreport.projectdailyreport.dto.request;
import az.projectdailyreport.projectdailyreport.model.Role;
import az.projectdailyreport.projectdailyreport.model.Team;
import az.projectdailyreport.projectdailyreport.validation.CrocusoftEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;


@Data
public class CreateUserRequest {


    @NotBlank(message = "Ad boş olamaz")
    private String firstName;

    @NotBlank(message = "Soyad boş olamaz")
    private String lastName;

    @NotBlank(message = "Şifre boş olamaz")
    private String password;

    @NotNull(message = "Rol belirtilmelidir")
    private Role role;


//    @CrocusoftEmail
    @Email(message = "Geçerli bir e-posta adresi değil")
    private String mail;

    @NotNull(message = "Takım belirtilmelidir")
    private Long teamId;




//    public CreateUserRequest(String userName, String firstName, String lastName, String password, Role role,     String mail, Team team, Set<Project> projects) {
//        this.userName = userName;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.password = password;
//        this.role = role;
//        this.mail = mail;
//        this.projects = projects;
//    }
    // Getter ve setter metotları
}


