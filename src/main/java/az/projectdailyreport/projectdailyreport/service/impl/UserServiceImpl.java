package az.projectdailyreport.projectdailyreport.service.impl;

import az.projectdailyreport.projectdailyreport.dto.*;
import az.projectdailyreport.projectdailyreport.dto.project.ProjectDTO;
import az.projectdailyreport.projectdailyreport.dto.request.AuthenticationResponse;
import az.projectdailyreport.projectdailyreport.dto.request.CreateUserRequest;
import az.projectdailyreport.projectdailyreport.exception.MailAlreadyExistsException;
import az.projectdailyreport.projectdailyreport.exception.SuperAdminException;
import az.projectdailyreport.projectdailyreport.exception.TeamNotFoundException;
import az.projectdailyreport.projectdailyreport.exception.UserNotFoundException;
import az.projectdailyreport.projectdailyreport.model.RoleName;
import az.projectdailyreport.projectdailyreport.model.Team;
import az.projectdailyreport.projectdailyreport.model.User;
import az.projectdailyreport.projectdailyreport.model.Status;
import az.projectdailyreport.projectdailyreport.repository.TeamRepository;
import az.projectdailyreport.projectdailyreport.repository.UserRepository;
import az.projectdailyreport.projectdailyreport.service.UserService;
import az.projectdailyreport.projectdailyreport.unit.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    //    private final ProjectService projectService;
//    private final RoleRepository roleRepository;
    private final TeamRepository teamRepository;
//    private final ProjectRepository projectRepository;

    @Override
    public List<UserGetAll> getAll(){
        List<User> userGetAlls = userRepository.findAll();

        List< UserGetAll> us = userGetAlls.stream().map(user -> {
            String fullName = user.getFirstName() + " " + user.getLastName();

            UserGetAll userGetAll =   UserGetAll.builder().id(user.getId())
                    .fullName(fullName)
                    .mail(user.getMail()).build();
            return userGetAll;

        }).collect(Collectors.toList());
        return us;
    }

    public User getSignedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public User createUser(CreateUserRequest createUserRequest) {
        // CreateUserRequest'ten User entity'si oluştur
        User user = new User();
        user.setFirstName(createUserRequest.getFirstName());
        user.setLastName(createUserRequest.getLastName());
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        user.setRole(createUserRequest.getRole());
        user.setStatus(Status.ACTIVE);

        if (createUserRequest.getRole().getId()==2){
            user.setRoleName(RoleName.ADMIN);
        }
        if (createUserRequest.getRole().getId()==3){
            user.setRoleName(RoleName.HEAD);
        }
        if (createUserRequest.getRole().getId()==4){
            user.setRoleName(RoleName.USER);
        }
        else {
            throw new SuperAdminException("icaze yoxdur");
        }

        user.setMail(createUserRequest.getMail());
        boolean isMailExists = userRepository.existsByMail(createUserRequest.getMail());
        if (isMailExists) {
            throw new MailAlreadyExistsException("Email address already exists");
        }

        if (createUserRequest.getTeamId()!=null){

            Team team =teamRepository.findById(createUserRequest.getTeamId())
                    .orElseThrow(() -> new TeamNotFoundException(createUserRequest.getTeamId()));

            user.setTeam(team);

        }

        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.createToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        authenticationService.saveUserToken(savedUser, jwtToken);
        return userRepository.save(user);

    }

    @Override
    public List<User> getUsersByIds(List<Long> userIds) {
        List<User> userList = userRepository.findAllById(userIds);
        return  userList;}

    @Override
    @Transactional
    public void softDeleteUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setDeleted(true);
            userRepository.save(user);
        } else {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }
    }

    @Override
    public Page<UserDTO> getUsersByFilters(String firstName, String lastName, Status status,List< Long> teamId, List<Long> projectIds, Pageable pageable) {        // UserRepository'den sayfalama ile kullanıcıları alma
        Page<User> usersPage = userRepository.findByFilters(firstName, lastName, status, teamId, projectIds, pageable);
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

    @Override
    @Transactional
    public UserDTO updateUser(Long userId, UserUpdateDTO updatedUserDTO) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();


            // ModelMapper kullanarak alanları eşleme
            modelMapper.map(updatedUserDTO, existingUser);
            existingUser.getTeam().setId(updatedUserDTO.getTeam().getId());
            existingUser.getTeam().setTeamName(updatedUserDTO.getTeam().getTeamName());

            userRepository.save(existingUser);

            return UserMapper.toDTO(existingUser);
        } else {
            // Kullanıcı bulunamadı, isteğe bağlı olarak bir hata yönetimi yapılabilir.
            throw new EntityNotFoundException("User not found with id: " + userId);
        }
    }

    @Override
    public void changeUserStatus(Long userId, Status newStatus) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException( userId));
        user.setStatus(newStatus);
        userRepository.save(user);
    }

}








