package az.projectdailyreport.projectdailyreport.service.impl;

import az.projectdailyreport.projectdailyreport.dto.request.CreateUserRequest;
import az.projectdailyreport.projectdailyreport.model.User;
import az.projectdailyreport.projectdailyreport.repository.RoleRepository;
import az.projectdailyreport.projectdailyreport.repository.TeamRepository;
import az.projectdailyreport.projectdailyreport.repository.UserRepository;
import az.projectdailyreport.projectdailyreport.service.UserService;
import lombok.RequiredArgsConstructor;
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
        user.setStatus(createUserRequest.getStatus());
        user.setMail(createUserRequest.getMail());
        user.setTeam(createUserRequest.getTeam());
        user.setProjects(new HashSet<>(createUserRequest.getProjects()));

        // User'ı kaydet
        return userRepository.save(user);
    }
}

