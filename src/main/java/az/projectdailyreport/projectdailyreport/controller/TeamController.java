package az.projectdailyreport.projectdailyreport.controller;
import az.projectdailyreport.projectdailyreport.dto.UserDTO;
import az.projectdailyreport.projectdailyreport.dto.team.TeamDTO;
import az.projectdailyreport.projectdailyreport.dto.TeamResponse;
import az.projectdailyreport.projectdailyreport.dto.team.TeamGetByIdDto;
import az.projectdailyreport.projectdailyreport.model.Team;
import az.projectdailyreport.projectdailyreport.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;



    @GetMapping("/get")
    public List<Team> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamGetByIdDto> getTeamById(@PathVariable Long id) {
        TeamGetByIdDto teamDto = teamService.getById(id);

        if (teamDto != null) {
            return new ResponseEntity<>(teamDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

}
    @PostMapping("/{teamId}/users/{userId}")
    public ResponseEntity<String> addUserToTeam(@PathVariable Long teamId, @PathVariable Long userId) {
        teamService.addUserToTeam(teamId, userId);
        return ResponseEntity.ok("User successfully added to the team.");

    }

    @DeleteMapping("/{teamId}/users/{userId}")
    public ResponseEntity<String> removeUserFromTeam(@PathVariable Long teamId, @PathVariable Long userId) {
        teamService.removeUserFromTeam(teamId, userId);
        return ResponseEntity.ok("User successfully removed from the team.");
    }

    @PostMapping("/create")
    public ResponseEntity<Team> createTeam(@RequestBody TeamResponse teamDto) {
        Team createdTeam = teamService.createTeam(teamDto);
        return new ResponseEntity<>(createdTeam, HttpStatus.CREATED);
    }
    @PutMapping("/{teamId}")
    public ResponseEntity<TeamDTO> updateTeam(@PathVariable Long teamId,
                                              @RequestBody TeamResponse updatedTeamDto) {
        Team updatedTeam = teamService.updateTeam(teamId, updatedTeamDto);
        TeamDTO responseDto = convertToDto(updatedTeam);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    private TeamDTO convertToDto(Team team) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(team, TeamDTO.class);

}


}

