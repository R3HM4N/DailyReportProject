package az.projectdailyreport.projectdailyreport.unit;

import az.projectdailyreport.projectdailyreport.dto.*;
import az.projectdailyreport.projectdailyreport.model.Project;
import az.projectdailyreport.projectdailyreport.model.User;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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


}
