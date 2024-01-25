package az.projectdailyreport.projectdailyreport.service;

import az.projectdailyreport.projectdailyreport.dto.TeamDTO;
import az.projectdailyreport.projectdailyreport.dto.TeamResponse;
import az.projectdailyreport.projectdailyreport.model.Team;

import java.util.List;
import java.util.Optional;

public interface TeamService {

    List<Team> getAllTeams();
    Optional<Team> getTeamById(Long id);
    Team updateTeam(Long teamId, TeamResponse updatedTeamDto);
    Team createTeam(TeamResponse teamDto);
//    void deleteTeam(Long id);
//    void softDeleteTeam(Long id);
}
