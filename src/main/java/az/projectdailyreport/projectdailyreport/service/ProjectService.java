package az.projectdailyreport.projectdailyreport.service;

import az.projectdailyreport.projectdailyreport.dto.project.*;
import az.projectdailyreport.projectdailyreport.dto.request.ProjectRequest;
import az.projectdailyreport.projectdailyreport.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {

    ProjectResponse createProject(ProjectRequest projectRequest);
    List<ProjectGetDto> getProjectsByUserId(Long userId);
    Project updateProjectAndUsers(Long projectId, ProjectUpdateDto newProjectName, List<Long> newUserIds);
    Page<ProjectFilterDto> searchProjectsByName(String projectName,Pageable pageable);

    List<ProjectGetDto> getAllProject();
    ProjectDTO convertToDto(Project project);
    Project getProjectById(Long projectId);
    ProjectFilterDto getById(Long projectId);
    List<Project> getProjectByIds(List <Long> projectId);

//    void softDeleteProject(Long id);

//    void hardDeleteProject(Long id);
}
