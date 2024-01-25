package az.projectdailyreport.projectdailyreport.model;

import az.projectdailyreport.projectdailyreport.validation.CrocusoftEmail;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@RequiredArgsConstructor
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    List<Project> projects;


    @OneToMany(mappedBy = "user")
    private Set<DailyReport> dailyReports;



}
