package az.projectdailyreport.projectdailyreport.model;
import az.projectdailyreport.projectdailyreport.validation.CrocusoftEmail;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder.Default
    private boolean deleted = false;



    @CrocusoftEmail
    @Builder.Default
    @Email(message = "Geçerli bir e-posta adresi değil")
    private String mail = null;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "team_id")
    @JsonIgnore
    private Team team;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users",
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonBackReference
    List<Project> projects;
    private String resetToken; // Şifre sıfırlama token'ı

    private LocalDateTime resetTokenCreationTime;




    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private Set<DailyReport> dailyReports;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(roleName.name()));
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return mail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }




}
