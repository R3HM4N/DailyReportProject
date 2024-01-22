package az.projectdailyreport.projectdailyreport.controller;

import az.projectdailyreport.projectdailyreport.dto.ProjectDto;
import az.projectdailyreport.projectdailyreport.dto.TeamDto;
import az.projectdailyreport.projectdailyreport.model.Project;
import az.projectdailyreport.projectdailyreport.model.Team;
import az.projectdailyreport.projectdailyreport.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/get")
    public List<Project> getAllProjects() {
        return projectService.getAllProject();
    }
    @PostMapping("/create")
    public ResponseEntity<Project> createProject(@RequestBody ProjectDto projectDto) {
       Project project =projectService.createProject(projectDto);
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @DeleteMapping("/soft/{id}")
    public ResponseEntity<String> softDeleteProject(@PathVariable Long id) {
            projectService.softDeleteProject(id);
            return ResponseEntity.ok("Project successfully SOFT Delete edildi.");
    }

    // Hard Delete
    @DeleteMapping("/hard/{id}")
    public ResponseEntity<String> hardDeleteProject(@PathVariable Long id) {

            projectService.hardDeleteProject(id);
            return ResponseEntity.ok("Project successfully HARD Delete edildi.");

    }
}
