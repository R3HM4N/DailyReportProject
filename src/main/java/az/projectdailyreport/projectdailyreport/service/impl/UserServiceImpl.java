package az.projectdailyreport.projectdailyreport.service.impl;

import az.projectdailyreport.projectdailyreport.dto.ProjectDTO;
import az.projectdailyreport.projectdailyreport.dto.UserDTO;
import az.projectdailyreport.projectdailyreport.dto.UserGetDTO;
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
import az.projectdailyreport.projectdailyreport.unit.PageInfo;
import az.projectdailyreport.projectdailyreport.unit.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
//    private final ProjectService projectService;
//    private final RoleRepository roleRepository;
//    private final TeamRepository teamRepository;
//    private final ProjectRepository projectRepository;


    @Override
    public User createUser(CreateUserRequest createUserRequest) {
        // CreateUserRequest'ten User entity'si oluştur
        User user = new User();
        user.setFirstName(createUserRequest.getFirstName());
        user.setLastName(createUserRequest.getLastName());
        user.setPassword(createUserRequest.getPassword());
        user.setRole(createUserRequest.getRole());
        user.setStatus(Status.ACTIVE);
        user.setMail(createUserRequest.getMail());
        user.setTeam(createUserRequest.getTeam());

        // User'ı kaydet
        return userRepository.save(user);
    }
    @Override
    public List<User> getUsersByIds(List<Long> userIds) {
        List<User> userList = userRepository.findAllById(userIds);
        return  userList;}

    @Override
    @Transactional
    public void hardDeleteUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        userRepository.deleteById(userId);
    }

    @Override
    public Page<UserDTO> getUsersByFilters(String firstName, String lastName, Status status, Long teamId, List<Long> projectIds, Pageable pageable) {
        // UserRepository'den sayfalama ile kullanıcıları alma
        Page<User> usersPage = userRepository.findByFilters(firstName, lastName, status, teamId, projectIds, pageable);

        // User listesini UserDTO listesine dönüştürme
        List<UserDTO> userDTOList = usersPage.getContent()
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());

        // UserDTO listesini kullanarak yeni bir Page oluşturma
        return new PageImpl<>(userDTOList, pageable, usersPage.getTotalElements());
    }

    @Override
    @Transactional
    public UserGetDTO getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!Hibernate.isInitialized(user.getProjects())) {
                Hibernate.initialize(user.getProjects());
            }

            UserGetDTO userGetDTO = UserMapper.toByDTO(user);
            List<ProjectDTO> projectDTOs = user.getProjects()
                    .stream()
                    .map(project -> modelMapper.map(project, ProjectDTO.class))
                    .collect(Collectors.toList());

            userGetDTO.setProjects(projectDTOs);
            return userGetDTO;
        } else {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }
    }













}








