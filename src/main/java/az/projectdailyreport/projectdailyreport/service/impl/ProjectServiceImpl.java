package az.projectdailyreport.projectdailyreport.service.impl;

import az.projectdailyreport.projectdailyreport.dto.ProjectDTO;
import az.projectdailyreport.projectdailyreport.dto.ProjectResponse;
import az.projectdailyreport.projectdailyreport.dto.request.ProjectRequest;
import az.projectdailyreport.projectdailyreport.exception.ProjectAlreadyDeletedException;
import az.projectdailyreport.projectdailyreport.exception.ProjectExistsException;
import az.projectdailyreport.projectdailyreport.exception.ProjectNotFoundException;
import az.projectdailyreport.projectdailyreport.model.Deleted;
import az.projectdailyreport.projectdailyreport.model.Project;
import az.projectdailyreport.projectdailyreport.model.Status;
import az.projectdailyreport.projectdailyreport.model.User;
import az.projectdailyreport.projectdailyreport.repository.ProjectRepository;
import az.projectdailyreport.projectdailyreport.service.ProjectService;
import az.projectdailyreport.projectdailyreport.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {


    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;



    @Override
    @Transactional
    public ProjectResponse createProject(ProjectRequest projectRequest) {
        if (projectRepository.existsByProjectName(projectRequest.getProjectName())) {
            throw new ProjectExistsException("A project with the same name already exists.");
        }

        Project project = modelMapper.map(projectRequest, Project.class);
        project.setStatus(Status.ACTIVE);

        List<Long> userIds = projectRequest.getUserIds();
        if (userIds != null && !userIds.isEmpty()) {
            List<User> users = userService.getUsersByIds(userIds);
            project.setUsers(users);
        }

        Project savedProject = projectRepository.save(project);

        // Convert to DTO using ModelMapper
        return modelMapper.map(savedProject, ProjectResponse.class);
    }


    @Override
    public List<Project> getAllProject() {
        return projectRepository.findAll();
    }

    @Override
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

    public Project updateProject(Long projectId, ProjectRequest updatedProjectRequest) {
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException( projectId));

        String updatedProjectName = updatedProjectRequest.getProjectName();

        // Check if the updated project name is not already in use by another project
        if (!existingProject.getProjectName().equals(updatedProjectName) &&
                projectRepository.existsByProjectName(updatedProjectName)) {
            throw new ProjectExistsException("Another project with the same name already exists.");
        }

        // Update project fields
        existingProject.setProjectName(updatedProjectName);
        // You can also update other fields if needed

        // Save the updated project
        return projectRepository.save(existingProject);
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
