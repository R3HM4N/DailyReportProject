package az.projectdailyreport.projectdailyreport.service.impl;

import az.projectdailyreport.projectdailyreport.dto.RoleDTO;
import az.projectdailyreport.projectdailyreport.dto.TeamResponse;
import az.projectdailyreport.projectdailyreport.dto.UserDTO;
import az.projectdailyreport.projectdailyreport.dto.team.TeamGetByIdDto;
import az.projectdailyreport.projectdailyreport.dto.team.TeamUserDto;
import az.projectdailyreport.projectdailyreport.exception.TeamExistsException;
import az.projectdailyreport.projectdailyreport.exception.TeamNotEmptyException;
import az.projectdailyreport.projectdailyreport.exception.TeamNotFoundException;
import az.projectdailyreport.projectdailyreport.exception.UserNotFoundException;
import az.projectdailyreport.projectdailyreport.model.Status;
import az.projectdailyreport.projectdailyreport.model.Team;
import az.projectdailyreport.projectdailyreport.model.User;
import az.projectdailyreport.projectdailyreport.repository.TeamRepository;
import az.projectdailyreport.projectdailyreport.repository.UserRepository;
import az.projectdailyreport.projectdailyreport.service.TeamService;
import az.projectdailyreport.projectdailyreport.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;



    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }
    @Override
    @Transactional
    public void removeUserFromTeam(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        Optional<User> userOptional = team.getUsers().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst();

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            team.getUsers().remove(user);
            user.setTeam(null); // Kullanıcının takım referansını temizleyin
            userRepository.save(user); // Kullanıcıyı güncelleyin
        } else {
            throw new UserNotFoundException(userId);
        }
        teamRepository.save(team);
    }

    @Override
    @Transactional
    public void addUserToTeam(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.setTeam(team);
        team.getUsers().add(user);

        teamRepository.save(team);
    }
    @Override
    public TeamGetByIdDto getById(Long id) {
        Team team = teamRepository.findById(id).orElse(null);
        List<TeamUserDto> userDtos = team.getUsers().stream().map(x -> {
            TeamUserDto dt = TeamUserDto.builder()
                    .id(x.getId())
                    .firstName(x.getFirstName())
                    .lastName(x.getLastName())
                    .role(RoleDTO.builder()
                            .id(x.getRole().getId())
                            .roleName(x.getRole().getRoleName())
                            .build())

                    .email(x.getMail())
                    .build();
            return dt;
        }).collect(Collectors.toList());

        TeamGetByIdDto dto = TeamGetByIdDto.builder()
                .id(team.getId())
                .name(team.getTeamName())
                .users(userDtos)

                .build();
        return dto;
    }
    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")

    public Team createTeam(TeamResponse teamDto) {
        String teamName = teamDto.getTeamName();

        if (teamRepository.existsByTeamName(teamName)) {
            throw new TeamExistsException("A team with the same name already exists.");
        }
        Team team = new Team();
        team.setTeamName(teamDto.getTeamName());
        team.setStatus(Status.ACTIVE);
        return teamRepository.save(team);
    }

    @Override
    public Team updateTeam(Long teamId, TeamResponse updatedTeamDto) {
        Team existingTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        String updatedTeamName = updatedTeamDto.getTeamName();

        // Check if the updated team name is not already in use by another team
        if (!existingTeam.getTeamName().equals(updatedTeamName) &&
                teamRepository.existsByTeamName(updatedTeamName)) {
            throw new TeamExistsException("Another team with the same name already exists.");
        }

        // Use ModelMapper to map fields from updatedTeamDto to existingTeam
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(updatedTeamDto, existingTeam);

        // You can also update other fields if needed

        // Save the updated team
        return teamRepository.save(existingTeam);

    }
    @Override
    @Transactional
    public void deleteTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        if (team.canBeDeleted()) {
            teamRepository.delete(team);
        } else {
            throw new TeamNotEmptyException(teamId);
        }
    }




//    @Override
//    @Transactional
//    public void softDeleteTeam(Long id) {
//        Team team = teamRepository.findById(id)
//                .orElseThrow(() -> new TeamNotFoundException(id));
//
//        if (team.getStatus() == Status.DELETED) {
//            throw new TeamAlreadyDeletedException(id);
//        }
//
//        teamRepository.softDeleteTeam(id);
//    }

}
