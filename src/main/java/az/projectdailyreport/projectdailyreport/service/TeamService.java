package az.projectdailyreport.projectdailyreport.service;

import az.projectdailyreport.projectdailyreport.dto.TeamDto;
import az.projectdailyreport.projectdailyreport.model.Team;

import java.util.List;
import java.util.Optional;

public interface TeamService {

    List<Team> getAllTeams();
    Optional<Team> getTeamById(Long id);
    Team createTeam(TeamDto teamDto);
//    void deleteTeam(Long id);
//    void softDeleteTeam(Long id);
}
