package az.projectdailyreport.projectdailyreport.service;

import az.projectdailyreport.projectdailyreport.dto.project.ProjectDTO;
import az.projectdailyreport.projectdailyreport.dto.project.ProjectGetDto;
import az.projectdailyreport.projectdailyreport.dto.project.ProjectResponse;
import az.projectdailyreport.projectdailyreport.dto.project.ProjectUpdateDto;
import az.projectdailyreport.projectdailyreport.dto.request.ProjectRequest;
import az.projectdailyreport.projectdailyreport.model.Project;

import java.util.List;

public interface ProjectService {

    ProjectResponse createProject(ProjectRequest projectRequest);
    Project updateProject(Long projectId, ProjectUpdateDto projectUpdateDto);
    void removeUserFromProject(Long projectId, Long userId);
    void addUserToProject(Long projectId, Long userId);
    List<ProjectGetDto> getAllProject();
    ProjectDTO convertToDto(Project project);
    Project getProjectById(Long projectId);
     List<Project> getProjectByIds(List <Long> projectId);

    void softDeleteProject(Long id);

//    void hardDeleteProject(Long id);
}
