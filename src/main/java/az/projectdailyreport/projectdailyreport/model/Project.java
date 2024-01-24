package az.projectdailyreport.projectdailyreport.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@RequiredArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Status status;

    private String projectName;

    //    @OneToMany(mappedBy = "project")
    //    private List<DailyReport> dailyReports;
//
//        @ManyToMany(mappedBy = "projects")
//        private Set<User> users = new HashSet<>();

}
