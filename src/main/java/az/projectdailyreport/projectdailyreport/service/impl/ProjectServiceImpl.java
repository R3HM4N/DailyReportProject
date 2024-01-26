package az.projectdailyreport.projectdailyreport.service.impl;

import az.projectdailyreport.projectdailyreport.dto.project.ProjectDTO;
import az.projectdailyreport.projectdailyreport.dto.project.ProjectGetDto;
import az.projectdailyreport.projectdailyreport.dto.project.ProjectResponse;
import az.projectdailyreport.projectdailyreport.dto.project.ProjectUpdateDto;
import az.projectdailyreport.projectdailyreport.dto.request.ProjectRequest;
import az.projectdailyreport.projectdailyreport.exception.ProjectAlreadyDeletedException;
import az.projectdailyreport.projectdailyreport.exception.ProjectExistsException;
import az.projectdailyreport.projectdailyreport.exception.ProjectNotFoundException;
import az.projectdailyreport.projectdailyreport.exception.UserNotFoundException;
import az.projectdailyreport.projectdailyreport.model.Deleted;
import az.projectdailyreport.projectdailyreport.model.Project;
import az.projectdailyreport.projectdailyreport.model.Status;
import az.projectdailyreport.projectdailyreport.model.User;
import az.projectdailyreport.projectdailyreport.repository.ProjectRepository;
import az.projectdailyreport.projectdailyreport.repository.UserRepository;
import az.projectdailyreport.projectdailyreport.service.ProjectService;
import az.projectdailyreport.projectdailyreport.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {


    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;



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
    public List<ProjectGetDto> getAllProject() {
     List<  Project> projects = projectRepository.findAll();

        return projects.stream()
                .map(project -> {
                    ProjectGetDto projectGetDto = new ProjectGetDto();
                    projectGetDto.setProjectName(project.getProjectName());
                    return projectGetDto;
                })
                .collect(Collectors.toList());
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

    public Project updateProject(Long projectId, ProjectUpdateDto projectUpdateDto) {
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException( projectId));


        String updatedProjectName = projectUpdateDto.getProjectName();

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
    public ProjectDTO convertToDto(Project project) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(project, ProjectDTO.class);
    }
    @Override
    @Transactional
    public void removeUserFromProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        User userToRemove = null;
        for (User user : project.getUsers()) {
            if (user.getId().equals(userId)) {
                userToRemove = user;
                break;
            }
        }

        if (userToRemove != null) {
            project.getUsers().remove(userToRemove);
            userToRemove.getProjects().remove(project);
            projectRepository.save(project);
        } else {
            throw new UserNotFoundException(userId);
        }
    }
    @Override
    @Transactional
    public void addUserToProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        project.getUsers().add(user);
        user.getProjects().add(project);
        projectRepository.save(project);
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
