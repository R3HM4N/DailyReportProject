package az.projectdailyreport.projectdailyreport.service.impl;

import az.projectdailyreport.projectdailyreport.dto.request.CreateUserRequest;
import az.projectdailyreport.projectdailyreport.exception.UserAlreadyDeletedException;
import az.projectdailyreport.projectdailyreport.exception.UserNotFoundException;
import az.projectdailyreport.projectdailyreport.model.User;
import az.projectdailyreport.projectdailyreport.model.Status;
import az.projectdailyreport.projectdailyreport.repository.RoleRepository;
import az.projectdailyreport.projectdailyreport.repository.TeamRepository;
import az.projectdailyreport.projectdailyreport.repository.UserRepository;
import az.projectdailyreport.projectdailyreport.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TeamRepository teamRepository;


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
        user.setProjects(new HashSet<>(createUserRequest.getProjects()));

        // User'ı kaydet
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void softDeleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (user.getStatus() == Status.DELETED) {
            throw new UserAlreadyDeletedException(userId);
        }

        try {
            userRepository.softDeleteUser(userId);
        } catch (EmptyResultDataAccessException ex) {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
    @Transactional
    public void hardDeleteUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        userRepository.deleteById(userId);
    }
}

