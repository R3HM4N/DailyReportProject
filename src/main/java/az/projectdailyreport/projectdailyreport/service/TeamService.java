package az.projectdailyreport.projectdailyreport.service;

import az.projectdailyreport.projectdailyreport.dto.TeamResponse;
import az.projectdailyreport.projectdailyreport.dto.UserDTO;
import az.projectdailyreport.projectdailyreport.dto.team.TeamGetByIdDto;
import az.projectdailyreport.projectdailyreport.model.Team;

import java.util.List;
import java.util.Optional;

public interface TeamService {

    List<Team> getAllTeams();
    void deleteTeam(Long teamId);
    //    TeamGetByIdDto getTeamById(Long teamId);
    void removeUserFromTeam(Long teamId, Long userId);
    void addUserToTeam(Long teamId, Long userId);
    TeamGetByIdDto getById(Long id);
    Team updateTeam(Long teamId, TeamResponse updatedTeamDto);
    Team createTeam(TeamResponse teamDto);
//    void deleteTeam(Long id);
//    void softDeleteTeam(Long id);
}
