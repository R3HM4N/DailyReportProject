package az.projectdailyreport.projectdailyreport.service.impl;

import az.projectdailyreport.projectdailyreport.dto.ProjectDto;
import az.projectdailyreport.projectdailyreport.exception.ProjectAlreadyDeletedException;
import az.projectdailyreport.projectdailyreport.exception.ProjectExistsException;
import az.projectdailyreport.projectdailyreport.exception.ProjectNotFoundException;
import az.projectdailyreport.projectdailyreport.model.Deleted;
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

    public List<Project> getProjectByIds(List <Long> projectId) {
        return projectRepository.findAllById(projectId);
    }
    @Override
    @Transactional
    public void softDeleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));

        if (Deleted.DELETED.equals(project.getStatus())) {
            throw new ProjectAlreadyDeletedException(id);
        }

        // Yeniden soft delete denemesini kontrol et
        if (projectRepository.existsByIdAndStatus(id, Deleted.DELETED)) {
            throw new ProjectAlreadyDeletedException(id);
        }

        projectRepository.softDeleteProject(id);
    }

    public Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

//    @Override
//    public void hardDeleteProject(Long id) {
//        Project project = projectRepository.findById(id)
//                .orElseThrow(() -> new ProjectNotFoundException(id));
//
//
//        projectRepository.deleteById(id);
//    }
}
