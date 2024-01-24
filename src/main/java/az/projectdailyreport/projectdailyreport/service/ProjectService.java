package az.projectdailyreport.projectdailyreport.service;

import az.projectdailyreport.projectdailyreport.dto.ProjectDTO;
import az.projectdailyreport.projectdailyreport.dto.request.ProjectRequest;
import az.projectdailyreport.projectdailyreport.model.Project;

import java.util.List;

public interface ProjectService {

    Project createProject(ProjectRequest projectRequest);
    List<Project> getAllProject();
    Project getProjectById(Long projectId);
     List<Project> getProjectByIds(List <Long> projectId);

    void softDeleteProject(Long id);

//    void hardDeleteProject(Long id);
}
