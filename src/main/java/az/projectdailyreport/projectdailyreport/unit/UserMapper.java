package az.projectdailyreport.projectdailyreport.unit;

import az.projectdailyreport.projectdailyreport.dto.*;
import az.projectdailyreport.projectdailyreport.model.Project;
import az.projectdailyreport.projectdailyreport.model.User;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Configuration

public class UserMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static UserDTO toDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public static UserGetDTO toByDTO(User user) {
        return modelMapper.map(user, UserGetDTO.class);
    }

    public static List<ProjectDTO> mapProjectsToDTO(Set<Project> projects) {
        Type listType = new TypeToken<List<ProjectDTO>>() {}.getType();
        return modelMapper.map(projects, listType);
    }
    public static ProjectDTO toProjectDTO(Project project) {
        return modelMapper.map(project, ProjectDTO.class);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Projelerin ve kullanıcıların karşılıklı olarak haritalanması için konfigürasyon
        modelMapper.addMappings(new PropertyMap<Project, ProjectDTO>() {
            @Override
            protected void configure() {
                // Projeden kullanıcıları hariç tutmamıza gerek yok, bu yüzden herhangi bir konfigürasyon eklememize gerek yok
            }
        });

        modelMapper.addMappings(new PropertyMap<User, UserGetDTO>() {
            @Override
            protected void configure() {
                // Kullanıcıdan projeleri hariç tutmamıza gerek yok, bu yüzden herhangi bir konfigürasyon eklememize gerek yok
            }
        });

        return modelMapper;
    }
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
