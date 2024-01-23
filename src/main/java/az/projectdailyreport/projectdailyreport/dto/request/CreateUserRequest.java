package az.projectdailyreport.projectdailyreport.dto.request;
import az.projectdailyreport.projectdailyreport.dto.TeamDto;
import az.projectdailyreport.projectdailyreport.model.Project;
import az.projectdailyreport.projectdailyreport.model.Role;
import az.projectdailyreport.projectdailyreport.model.Team;
import az.projectdailyreport.projectdailyreport.validation.CrocusoftEmail;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Set;


@Data
public class CreateUserRequest {

    @NotBlank(message = "Kullanıcı adı boş olamaz")
    private String userName;

    @NotBlank(message = "Ad boş olamaz")
    private String firstName;

    @NotBlank(message = "Soyad boş olamaz")
    private String lastName;

    @NotBlank(message = "Şifre boş olamaz")
    private String password;

    @NotNull(message = "Rol belirtilmelidir")
    private Role role;


    @CrocusoftEmail
    @Email(message = "Geçerli bir e-posta adresi değil")
    private String mail;

    @NotNull(message = "Takım belirtilmelidir")
    private Team team;

    private List<Long> projectIds;


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


