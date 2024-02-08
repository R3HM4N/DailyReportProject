package az.projectdailyreport.projectdailyreport.dto.project;

import az.projectdailyreport.projectdailyreport.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectFilterDto {

    private Long id;

    private Status status;
    private String projectName;
    private List<ProjectUserDto> users; // Assuming you have a UserDTO class

}
