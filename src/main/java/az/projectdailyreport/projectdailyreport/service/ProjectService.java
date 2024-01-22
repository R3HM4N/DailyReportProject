package az.projectdailyreport.projectdailyreport.service;

import az.projectdailyreport.projectdailyreport.dto.ProjectDto;
import az.projectdailyreport.projectdailyreport.model.Project;
import az.projectdailyreport.projectdailyreport.model.Team;

import java.util.List;

public interface ProjectService {

    Project createProject(ProjectDto projectDto);
    List<Project> getAllProject();

    void softDeleteProject(Long id);

    void hardDeleteProject(Long id);
}
