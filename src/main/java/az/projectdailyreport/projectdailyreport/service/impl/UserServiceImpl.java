package az.projectdailyreport.projectdailyreport.service.impl;

import az.projectdailyreport.projectdailyreport.dto.request.CreateUserRequest;
import az.projectdailyreport.projectdailyreport.exception.UserAlreadyDeletedException;
import az.projectdailyreport.projectdailyreport.exception.UserNotFoundException;
import az.projectdailyreport.projectdailyreport.model.Project;
import az.projectdailyreport.projectdailyreport.model.User;
import az.projectdailyreport.projectdailyreport.model.Status;
import az.projectdailyreport.projectdailyreport.repository.ProjectRepository;
import az.projectdailyreport.projectdailyreport.repository.RoleRepository;
import az.projectdailyreport.projectdailyreport.repository.TeamRepository;
import az.projectdailyreport.projectdailyreport.repository.UserRepository;
import az.projectdailyreport.projectdailyreport.service.ProjectService;
import az.projectdailyreport.projectdailyreport.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProjectService projectService;
    private final RoleRepository roleRepository;
    private final TeamRepository teamRepository;
    private final ProjectRepository projectRepository;


    @Override
    public User createUser(CreateUserRequest createUserRequest) {
        // CreateUserRequest'ten User entity'si oluştur
        User user = new User();
        user.setUserName(createUserRequest.getUserName());
        user.setFirstName(createUserRequest.getFirstName());
        user.setLastName(createUserRequest.getLastName());
        user.setPassword(createUserRequest.getPassword());
        user.setRole(createUserRequest.getRole());
        user.setStatus(Status.ACTIVE);
        user.setMail(createUserRequest.getMail());
        user.setTeam(createUserRequest.getTeam());
        user.setProjects(new HashSet<>(projectService.getProjectByIds(createUserRequest.getProjectIds())));

        // User'ı kaydet
        return userRepository.save(user);
    }

    public List<User> getUsersWithFilters(Status status, String firstName, String lastName, List<Long> projectIds) {
        List<Project> projects = convertProjectIdsToProjects(projectIds);

        List<User> users = userRepository.findAllWithFilters(status, firstName, lastName,projects);

        // Proje filtreleme işlemi
        List<User> filteredUsers = users.stream()
                .filter(user -> user.getProjects().containsAll(projects))
                .collect(Collectors.toList());

        return filteredUsers;
    }
    private List<Project> convertProjectIdsToProjects(List<Long> projectIds) {
        if (projectIds == null || projectIds.isEmpty()) {
            return Collections.emptyList(); // Eğer proje ID'leri null veya boş ise boş bir liste döndür
        }

        else {
        return projectRepository.findAllById(projectIds);
    }}

    @Override
    @Transactional
    public void hardDeleteUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        userRepository.deleteById(userId);
    }
}

