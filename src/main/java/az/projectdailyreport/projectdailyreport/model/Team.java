package az.projectdailyreport.projectdailyreport.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Data
@RequiredArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status;
    private String teamName;

    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JsonBackReference

    private List<User> users;

    public boolean canBeDeleted() {
        return users == null || users.isEmpty();
    }


}

