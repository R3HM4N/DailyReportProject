package az.projectdailyreport.projectdailyreport.unit;

import az.projectdailyreport.projectdailyreport.dto.*;
import az.projectdailyreport.projectdailyreport.dto.project.ProjectDTO;
import az.projectdailyreport.projectdailyreport.model.Project;
import az.projectdailyreport.projectdailyreport.model.User;
import az.projectdailyreport.projectdailyreport.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Configuration
@RequiredArgsConstructor

public class UserMapper {
    private static final ModelMapper modelMapper = new ModelMapper();
    private final UserRepository repository;


    public static UserDTO toDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public static UserGetDTO toByDTO(User user) {
        return modelMapper.map(user, UserGetDTO.class);
    }



    @Bean
    public UserDetailsService userDetailsService() {
        return mail -> repository.findByEmail(mail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static List<ProjectDTO> mapProjectsToDTO(Set<Project> projects) {
        Type listType = new TypeToken<List<ProjectDTO>>() {}.getType();
        return modelMapper.map(projects, listType);
    }
    public static ProjectDTO toProjectDTO(Project project) {
        return modelMapper.map(project, ProjectDTO.class);
    }

//    @Bean
//    public ModelMapper modelMapper() {
//        ModelMapper modelMapper = new ModelMapper();
//
//        modelMapper.addMappings(new PropertyMap<Project, ProjectDTO>() {
//            @Override
//            protected void configure() {
//            }
//        });
//
//        modelMapper.addMappings(new PropertyMap<User, UserGetDTO>() {
//            @Override
//            protected void configure() {
//                // Kullanıcıdan projeleri hariç tutmamıza gerek yok, bu yüzden herhangi bir konfigürasyon eklememize gerek yok
//            }
//        });
//
//        return modelMapper;
//    }
    public UserGetDTO toBy1DTO(User user) {
        UserGetDTO userGetDTO = modelMapper.map(user, UserGetDTO.class);

        // Projeleri ProjectDTO'ya dönüştürerek set et
        List<ProjectDTO> projectDTOs = user.getProjects()
                .stream()
                .map(project -> modelMapper.map(project, ProjectDTO.class))
                .collect(Collectors.toList());

        userGetDTO.setProjects(projectDTOs);

        return userGetDTO;
    }


}
