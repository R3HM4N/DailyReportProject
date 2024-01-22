package az.projectdailyreport.projectdailyreport.model;

import az.projectdailyreport.projectdailyreport.validation.CrocusoftEmail;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private String Password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;

    @CrocusoftEmail
    @Email(message = "Geçerli bir e-posta adresi değil")
    private String mail;

    @ManyToOne
    @JoinColumn(name = "team_id")  // Bu alan, User'ın hangi Team'e ait olduğunu belirtir
    private Team team;

    @ManyToMany
    @JoinTable(
            name = "user_projects",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects = new HashSet<>();
    @OneToMany(mappedBy = "user")
    private Set<DailyReport> dailyReports;

    public User(String userName) {
        this.userName = userName;
    }

    // Getter ve setter metotları
}
