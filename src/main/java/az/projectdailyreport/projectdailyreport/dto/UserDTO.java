package az.projectdailyreport.projectdailyreport.dto;

import az.projectdailyreport.projectdailyreport.dto.project.ProjectDTO;
import az.projectdailyreport.projectdailyreport.dto.team.TeamDTO;
import az.projectdailyreport.projectdailyreport.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private  RoleDTO role;
    private Status status;
    private String mail;
    private TeamDTO team;
    private List<ProjectDTO> projects;


//    public void setProjects(List<ProjectDTO> projects) {
//        this.projects = projects;
//    }

}