package az.projectdailyreport.projectdailyreport.service.impl;

import az.projectdailyreport.projectdailyreport.dto.ProjectDto;
import az.projectdailyreport.projectdailyreport.exception.ProjectAlreadyDeletedException;
import az.projectdailyreport.projectdailyreport.exception.ProjectExistsException;
import az.projectdailyreport.projectdailyreport.exception.ProjectNotFoundException;
import az.projectdailyreport.projectdailyreport.model.Project;
import az.projectdailyreport.projectdailyreport.model.Status;
import az.projectdailyreport.projectdailyreport.repository.ProjectRepository;
import az.projectdailyreport.projectdailyreport.service.ProjectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {


    private final ProjectRepository projectRepository;


    @Override
    public Project createProject(ProjectDto projectDto) {
        if (projectRepository.existsByProjectName(projectDto.getProjectName())) {
            // Proje zaten varsa EntityExistsException fırlat
            throw new ProjectExistsException("A project with the same name already exists.");
        }
        Project project = new Project();
        project.setProjectName(projectDto.getProjectName());
        project.setStatus(Status.ACTIVE);
        return projectRepository.save(project);
    }

    @Override
    public List<Project> getAllProject() {
        return projectRepository.findAll();
    }

    @Override
    @Transactional
    public void softDeleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));

        if (Status.DELETED.equals(project.getStatus())) {
            throw new ProjectAlreadyDeletedException(id);
        }

        // Yeniden soft delete denemesini kontrol et
        if (projectRepository.existsByIdAndStatus(id, Status.DELETED)) {
            throw new ProjectAlreadyDeletedException(id);
        }

        projectRepository.softDeleteProject(id);
    }

    @Override
    public void hardDeleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));


        projectRepository.deleteById(id);
    }
}
