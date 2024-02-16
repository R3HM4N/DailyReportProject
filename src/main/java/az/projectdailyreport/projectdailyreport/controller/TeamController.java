package az.projectdailyreport.projectdailyreport.controller;
import az.projectdailyreport.projectdailyreport.dto.UserDTO;
import az.projectdailyreport.projectdailyreport.dto.team.TeamDTO;
import az.projectdailyreport.projectdailyreport.dto.TeamResponse;
import az.projectdailyreport.projectdailyreport.dto.team.TeamGetByIdDto;
import az.projectdailyreport.projectdailyreport.exception.TeamNotFoundException;
import az.projectdailyreport.projectdailyreport.exception.UserNotFoundException;
import az.projectdailyreport.projectdailyreport.model.Team;
import az.projectdailyreport.projectdailyreport.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;



    @GetMapping
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
    @PutMapping("/{teamId}")
    public ResponseEntity<TeamResponse> updateTeamAndUsers(
            @PathVariable Long teamId,
            @RequestBody TeamResponse teamResponse,
            @RequestParam(required = false) List<Long> userIdsToAdd
    ) {
        TeamResponse updatedTeam = teamService.updateTeamAndUsers(teamId, teamResponse, userIdsToAdd);
        return ResponseEntity.ok(updatedTeam);
    }


    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody TeamResponse teamDto) {
        Team createdTeam = teamService.createTeam(teamDto);
        return new ResponseEntity<>(createdTeam, HttpStatus.CREATED);
    }

    private TeamDTO convertToDto(Team team) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(team, TeamDTO.class);

    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<String> deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
        return new ResponseEntity<>("Team deleted successfully", HttpStatus.OK);


    }


}

