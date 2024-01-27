package az.projectdailyreport.projectdailyreport.dto.project;

import az.projectdailyreport.projectdailyreport.dto.UserDTO;
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
public class ProjectFiltir {

    private Long id;
    private Status status;
    private String projectName;
    private List<UserDTO> users; // Assuming you have a UserDTO class

    private int totalPages;
    private long totalElements;
    private boolean hasNext;
}
