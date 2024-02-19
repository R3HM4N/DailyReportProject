package az.projectdailyreport.projectdailyreport.service.impl;

import az.projectdailyreport.projectdailyreport.dto.RoleDTO;
import az.projectdailyreport.projectdailyreport.dto.TeamResponse;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
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
    @Transactional
    public TeamResponse updateTeamAndUsers(Long teamId, TeamResponse newTeamName, List<Long> newUserIds) {
        Team existingTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));
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

        existingTeam = teamRepository.save(existingTeam);

        TeamResponse updatedTeamResponse = new TeamResponse();
        updatedTeamResponse.setTeamName(existingTeam.getTeamName());
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
            throw new TeamNotEmptyException(teamId);
        }
    }

}
