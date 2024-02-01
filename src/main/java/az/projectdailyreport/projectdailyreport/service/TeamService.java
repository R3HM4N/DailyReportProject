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
    TeamResponse updateTeamAndUsers(Long teamId, TeamResponse updatedTeamDto, List<Long> userIdsToAdd, List<Long> userIdsToRemove);
    TeamGetByIdDto getById(Long id);
    Team createTeam(TeamResponse teamDto);
//    void deleteTeam(Long id);
//    void softDeleteTeam(Long id);
}
