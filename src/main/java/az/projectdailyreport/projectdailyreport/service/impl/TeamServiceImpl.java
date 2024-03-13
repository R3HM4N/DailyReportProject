package az.projectdailyreport.projectdailyreport.service.impl;

import az.projectdailyreport.projectdailyreport.dto.RoleDTO;
import az.projectdailyreport.projectdailyreport.dto.TeamResponse;
import az.projectdailyreport.projectdailyreport.dto.team.TeamGetByIdDto;
import az.projectdailyreport.projectdailyreport.dto.team.TeamUserDto;
import az.projectdailyreport.projectdailyreport.exception.*;
import az.projectdailyreport.projectdailyreport.model.Status;
import az.projectdailyreport.projectdailyreport.model.Team;
import az.projectdailyreport.projectdailyreport.model.User;
import az.projectdailyreport.projectdailyreport.repository.TeamRepository;
import az.projectdailyreport.projectdailyreport.repository.UserRepository;
import az.projectdailyreport.projectdailyreport.service.TeamService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j

@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(TeamServiceImpl.class);




    @Override
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    public TeamGetByIdDto getById(Long id) {
        Team team = teamRepository.findById(id).orElse(null);
        List<TeamUserDto> userDtos = team.getUsers().stream()
                .filter(user -> !user.isDeleted())
                .map(x -> {
            TeamUserDto dt = TeamUserDto.builder()
                    .id(x.getId())
                    .firstName(x.getFirstName())
                    .lastName(x.getLastName())
                    .status(x.getStatus())
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

    public Team createTeam(TeamResponse teamDto) {
        String teamName = teamDto.getTeamName();

        if (teamRepository.existsByTeamName(teamName)) {
            throw new TeamExistsException("A team with the same name already exists.");
        }
        Team team = new Team();
        team.setTeamName(teamDto.getTeamName());
        team.setStatus(Status.ACTIVE);
        log.info("Team created");

        return teamRepository.save(team);

    }

    @Override
    @Transactional
    public TeamResponse updateTeamAndUsers(Long teamId, TeamResponse newTeamName, List<Long> newUserIds) {
        Team existingTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));
        Optional<Team> teamWithSameName = teamRepository.findByTeamNameIgnoreCase(newTeamName.getTeamName());
        if (teamWithSameName.isPresent() && !teamWithSameName.get().getId().equals(teamId)) {
            throw new TeamExistsException("Team already exists, please choose another name.");
        }
        existingTeam.setTeamName(newTeamName.getTeamName());
        if (newUserIds!=null){
        for (User user : existingTeam.getUsers()) {
            user.setTeam(null);
        }
        existingTeam.getUsers().clear();

        for (Long userId : newUserIds) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(userId));
            user.setTeam(existingTeam);
            existingTeam.getUsers().add(user);
        }}
        if (newUserIds==null){
            for (User user : existingTeam.getUsers()) {
                user.setTeam(null);
            }
        }
        existingTeam = teamRepository.save(existingTeam);

        TeamResponse updatedTeamResponse = new TeamResponse();
        updatedTeamResponse.setTeamName(existingTeam.getTeamName());
        log.info("Team updated");

        return updatedTeamResponse;
    }

    @Override
    @Transactional
    public void deleteTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        if (team.canBeDeleted()) {
            teamRepository.delete(team);
        } else {
            throw new MailAlreadyExistsException("Team  cannot be deleted as it contains users.");
        }
        log.info("Team deleted");

    }

}
