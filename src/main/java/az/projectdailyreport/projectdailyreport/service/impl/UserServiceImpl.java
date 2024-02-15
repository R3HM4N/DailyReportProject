package az.projectdailyreport.projectdailyreport.service.impl;

import az.projectdailyreport.projectdailyreport.dto.*;
import az.projectdailyreport.projectdailyreport.dto.project.ProjectDTO;
import az.projectdailyreport.projectdailyreport.dto.request.ConfirmPassword;
import az.projectdailyreport.projectdailyreport.dto.request.CreateUserRequest;
import az.projectdailyreport.projectdailyreport.dto.request.UserResetPasswordRequest;
import az.projectdailyreport.projectdailyreport.exception.*;
import az.projectdailyreport.projectdailyreport.model.*;
import az.projectdailyreport.projectdailyreport.repository.TeamRepository;
import az.projectdailyreport.projectdailyreport.repository.UserRepository;
import az.projectdailyreport.projectdailyreport.service.UserService;
import az.projectdailyreport.projectdailyreport.unit.EmailService;
import az.projectdailyreport.projectdailyreport.unit.UserMapper;
import ch.qos.logback.core.joran.conditional.IfAction;
import jakarta.mail.MessagingException;
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

import java.time.LocalDateTime;
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
    private final EmailService emailService;
    private final TeamRepository teamRepository;
    private final RoleServiceImpl roleService;

    @Override
    public List<UserGetAll> getAll() {
        List<User> userGetAlls = userRepository.findAll();

        List<UserGetAll> us = userGetAlls.stream().map(user -> {
            String fullName = user.getFirstName() + " " + user.getLastName();

            UserGetAll userGetAll = UserGetAll.builder().id(user.getId())
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
        User user1 = getSignedInUser();
        User user = new User();
        user.setFirstName(createUserRequest.getFirstName());
        user.setLastName(createUserRequest.getLastName());
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        user.setRole(createUserRequest.getRole());
        user.setStatus(Status.ACTIVE);

        if (user1.getRoleName().equals(RoleName.ADMIN) && (createUserRequest.getRole().getId() == 2 || createUserRequest.getRole().getId() == 1)) {
            throw new RoleException("Yoou don't have access to create Admin and SuperAdmin.");
        }
        if (user1.getRoleName().equals(RoleName.SUPER_ADMIN)) {
            if (createUserRequest.getRole().getId() == 2) {
                user.setRoleName(RoleName.ADMIN);
            } else if (createUserRequest.getRole().getId() == 3) {
                user.setRoleName(RoleName.HEAD);
            } else if (createUserRequest.getRole().getId() == 4) {
                user.setRoleName(RoleName.EMPLOYEE);
            }
        } else if (user1.getRoleName().equals(RoleName.ADMIN)) {
            if (createUserRequest.getRole().getId() == 3) {
                user.setRoleName(RoleName.HEAD);
            } else if (createUserRequest.getRole().getId() == 4) {
                user.setRoleName(RoleName.EMPLOYEE);
            }
        }

        user.setMail(createUserRequest.getMail());
        boolean isMailExists = userRepository.existsByMail(createUserRequest.getMail());
        if (isMailExists) {
            throw new MailAlreadyExistsException("Email address already exists");
        }

        if (createUserRequest.getTeamId() != null) {

            Team team = teamRepository.findById(createUserRequest.getTeamId())
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
        return userList;
    }

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
    public Page<UserDTO> getUsersByFilters(String firstName, String lastName, Status status, List<Long> teamId, List<Long> projectIds, Pageable pageable) {        // UserRepository'den sayfalama ile kullanıcıları alma

        User signeduser = getSignedInUser();
        if (signeduser.getRoleName().equals(RoleName.SUPER_ADMIN) || signeduser.getRoleName().equals(RoleName.HEAD)){
        Page<User> usersPage = userRepository.findByFilters(firstName, lastName, status, teamId, projectIds, pageable);
        List<UserDTO> userDTOList = usersPage.getContent()
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(userDTOList, pageable, usersPage.getTotalElements());}
        
            Page<User> userPage1 =userRepository.findByFiltersForAdmin(firstName, lastName, status, teamId, projectIds,signeduser.getId(), pageable);
            List<UserDTO> userDTOList = userPage1.getContent()
                    .stream()
                    .map(UserMapper::toDTO)
            .toList();
        return new PageImpl<>(userDTOList,pageable,userPage1.getTotalElements());

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

            User updatedUser = User.builder()
                    .id(existingUser.getId())
                    .firstName(updatedUserDTO.getFirstName())
                    .lastName(updatedUserDTO.getLastName())
                    .mail(updatedUserDTO.getEmail())
                    .build();
                Optional<Team> team = teamRepository.findById(updatedUserDTO.getTeamId());
                team.ifPresent(existingUser::setTeam);
                Optional<Role> role = roleService.findRoleById(updatedUserDTO.getRoleId());
                role.ifPresent(existingUser::setRole);


            userRepository.save(updatedUser);

            return UserMapper.toDTO(existingUser);
        } else {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }
    }



    @Override
    public String resetPassword(Long userId, UserReset userReset) {
        Optional<User> userOptional = userRepository.findById(userId);
        User user1 = getSignedInUser();
        if (userOptional.isPresent() && userReset.getPassword().equals(userReset.getNewConfirimPassword())) {
            User existingUser = userOptional.get();
            String encryptedPassword = passwordEncoder.encode(userReset.getPassword());
            existingUser.setPassword(encryptedPassword);
            String resetToken = UUID.randomUUID().toString();
            existingUser.setResetToken(resetToken);
            if ((user1.getRoleName().equals(RoleName.ADMIN)) && userOptional.get().getRoleName().equals(RoleName.SUPER_ADMIN) ||
                    (user1.getRoleName().equals(RoleName.ADMIN) && !user1.getId().equals(userId))) {

                throw new EntityNotFoundException("Password change failed: " + userId);

            } else {
                userRepository.save(existingUser);
            }
            return "Update is complete";

        } else {
            throw new EntityNotFoundException("Password change failed: " + userId);
        }
    }

    @Override
    public void changeUserStatus(Long userId, Status newStatus) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(userId));
        user.setStatus(newStatus);
        userRepository.save(user);
    }

    @Override
    public void changePassword(UserChangePassword changePassword) {
        User user = getSignedInUser();

        if (passwordEncoder.matches(changePassword.getOldpassword(), user.getPassword()) && changePassword.getNewPassword().equals(changePassword.getNewConfirimPassword())) {
            String encryptedPassword = passwordEncoder.encode(changePassword.getNewPassword());
            user.setPassword(encryptedPassword);
            userRepository.save(user);
        } else {
            throw new InvalidPasswordException("New passwords do not match OR Invalid password");
        }
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);

    }


    private void sendOtpByEmail(String email, String otp) {
        String subject = "Password Reset OTP";
        String message = "Şifrənizi yeniləmək üçün tələb göndərdiniz: \n Aşağıdakı koddan istifadə edərək şifrənizi yeniləyə bilərsiniz \n"
                + otp + " \n Bu kodu başqa şəxslərlə paylaşmayın" + "\n Hər hansı bir çətinliyiniz olarsa sistem administratoru ilə əlaqə saxlamağınız tövsiyyə olunur." + "\n Hörmətlə, Crocusoft ";

        try {
            emailService.sendSimpleMessage(email, subject, message);
        } catch (MessagingException e) {
            throw new EmailNotSentException("Failed to send OTP via email");
        }
    }

    @Override
    public void sendPasswordResetEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserMailNotFoundExeption("User not found with email: " + email));
        String otp = generateOtp();

        sendOtpByEmail(email, otp);

        user.setResetToken(otp);
        user.setResetTokenCreationTime(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void resetPasswordWithOtp(UserResetPasswordRequest forgetDto) {
        User user = userRepository.findByResetToken(forgetDto.getOtp())
                .orElseThrow(() -> new UserMailNotFoundExeption("User not found: "));

        if (isOtpValid(user.getResetTokenCreationTime())) {
            LocalDateTime currentTime = LocalDateTime.now(); // Doğru şekilde güncellendi
            LocalDateTime expirationTime = user.getResetTokenCreationTime().plusMinutes(5); // 5 dakika süre tanımla

            if (currentTime.isBefore(expirationTime)) {
                user.setChange(true);
                userRepository.save(user);
            } else {
                throw new InvalidOtpException("OTP has expired");
            }
        } else {
            throw new InvalidOtpException("Invalid OTP");
        }
    }

    private boolean isOtpValid(LocalDateTime creationTime) {
        if (creationTime == null) {
            return false;
        }
        LocalDateTime currentTime = LocalDateTime.now(); // Doğru şekilde güncellendi
        LocalDateTime expirationTime = creationTime.plusMinutes(5); // 5 dakika süre tanımla
        return currentTime.isBefore(expirationTime);
    }
    @Override
    public void confirmPassword(ConfirmPassword confirmPassword) {
        User user = userRepository.findByChangeIsTrue().orElseThrow(() -> new InvalidOtpException("expired OTP"));
        if (confirmPassword.getNewPassword().equals(confirmPassword.getConfirmNewPassword())) {
            String encryptedPassword = passwordEncoder.encode(confirmPassword.getNewPassword());
            user.setPassword(encryptedPassword);
            user.setResetToken(null);
            user.setChange(false);
            userRepository.save(user);
        } else {
            throw new PasswordsNotMatchException("New password and confirm password do not match");
        }

    }


}








