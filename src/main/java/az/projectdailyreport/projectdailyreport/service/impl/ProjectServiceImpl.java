package az.projectdailyreport.projectdailyreport.service.impl;

import az.projectdailyreport.projectdailyreport.dto.project.*;
import az.projectdailyreport.projectdailyreport.dto.request.ProjectRequest;
import az.projectdailyreport.projectdailyreport.dto.team.TeamDTO;
import az.projectdailyreport.projectdailyreport.exception.*;
import az.projectdailyreport.projectdailyreport.model.*;
import az.projectdailyreport.projectdailyreport.repository.ProjectRepository;
import az.projectdailyreport.projectdailyreport.repository.UserRepository;
import az.projectdailyreport.projectdailyreport.service.ProjectService;
import az.projectdailyreport.projectdailyreport.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

        return modelMapper.map(savedProject, ProjectResponse.class);
    }


    @Override
    public List<ProjectGetDto> getAllProject() {
        List<  Project> projects = projectRepository.findAll();

        return projects.stream()
                .map(project -> {
                    ProjectGetDto projectGetDto = new ProjectGetDto();
                    projectGetDto.setProjectName(project.getProjectName());
                    projectGetDto.setId(project.getId());

                    return projectGetDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Project> getProjectByIds(List <Long> projectId) {
        return projectRepository.findAllById(projectId);
    }




    public Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    @Override
    public ProjectFilterDto getById(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        return mapToProjectFilterDto(project);
    }


    public ProjectDTO convertToDto(Project project) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(project, ProjectDTO.class);
    }
    @Override
    @Transactional
    public Project updateProjectAndUsers(Long projectId, ProjectUpdateDto newProjectName, List<Long> newUserIds) {
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        existingProject.setProjectName(newProjectName.getProjectName());

        if (newUserIds!=null) {
            for (User user : existingProject.getUsers()) {
                user.getProjects().remove(existingProject);
            }
            existingProject.getUsers().clear();
            for (Long userId : newUserIds) {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException(userId));
                existingProject.getUsers().add(user);
                user.getProjects().add(existingProject);
            }
        }
        return projectRepository.save(existingProject);
    }

    @Override
    public Page<ProjectFilterDto> searchProjectsByName(String projectName, Pageable pageable) {
       if (projectName==null){
           Page<Project> projectss = projectRepository.findAll(pageable);
           List<ProjectFilterDto> projectFilterDtoss = mapToProjectFilterDtoList(projectss.getContent());
           return new PageImpl<>(projectFilterDtoss,pageable,projectss.getTotalElements());
       }
       else{ Page<Project> projects = projectRepository.findByProjectNameContainingIgnoreCase(projectName, pageable);
        List<ProjectFilterDto> projectFilterDtos = mapToProjectFilterDtoList(projects.getContent());
        return new PageImpl<>(projectFilterDtos, pageable, projects.getTotalElements());}
    }


    private List<ProjectFilterDto> mapToProjectFilterDtoList(List<Project> projects) {
        return projects.stream()
                .map(this::mapToProjectFilterDto)
                .collect(Collectors.toList());
    }

    private ProjectFilterDto mapToProjectFilterDto(Project project) {
        return ProjectFilterDto.builder()
                .id(project.getId())
                .status(project.getStatus())
                .projectName(project.getProjectName())
                .users(mapToProjectUserDtoList(project.getUsers()))
                .build();
    }

    private List<ProjectUserDto> mapToProjectUserDtoList(List<User> users) {
        return users.stream()
                .map(this::mapToProjectUserDto)
                .collect(Collectors.toList());
    }

    private ProjectUserDto mapToProjectUserDto(User user) {
        return ProjectUserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .status(user.getStatus())
                .mail(user.getMail())
                .team(user.getTeam() != null ? mapToTeamDto(user.getTeam()) : null)
                .build();
    }

    private TeamDTO mapToTeamDto(Team team) {
        return TeamDTO.builder()
                .teamName(team.getTeamName())
                .id(team.getId()).build();
    }
}