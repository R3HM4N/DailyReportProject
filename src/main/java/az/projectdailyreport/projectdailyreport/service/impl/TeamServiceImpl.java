package az.projectdailyreport.projectdailyreport.service.impl;

import az.projectdailyreport.projectdailyreport.dto.TeamDTO;
import az.projectdailyreport.projectdailyreport.dto.TeamResponse;
import az.projectdailyreport.projectdailyreport.exception.TeamExistsException;
import az.projectdailyreport.projectdailyreport.exception.TeamNotFoundException;
import az.projectdailyreport.projectdailyreport.model.Status;
import az.projectdailyreport.projectdailyreport.model.Team;
import az.projectdailyreport.projectdailyreport.repository.TeamRepository;
import az.projectdailyreport.projectdailyreport.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;



    @Override
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    public Optional<Team> getTeamById(Long id) {
        return teamRepository.findById(id);
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


//    @Override
//    public void deleteTeam(Long id) {
//        teamRepository.findById(id)
//                .orElseThrow(() -> new TeamNotFoundException(id));
//
//        teamRepository.deleteById(id);
//    }

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
