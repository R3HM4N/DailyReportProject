package az.projectdailyreport.projectdailyreport.controller;

import az.projectdailyreport.projectdailyreport.dto.project.ProjectDTO;
import az.projectdailyreport.projectdailyreport.dto.project.ProjectGetDto;
import az.projectdailyreport.projectdailyreport.dto.project.ProjectResponse;
import az.projectdailyreport.projectdailyreport.dto.project.ProjectUpdateDto;
import az.projectdailyreport.projectdailyreport.dto.request.ProjectRequest;
import az.projectdailyreport.projectdailyreport.model.Project;
import az.projectdailyreport.projectdailyreport.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    public List<ProjectGetDto> getAllProjects() {
        return projectService.getAllProject();
    }
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest projectRequest) {
        ProjectResponse projectResponse = projectService.createProject(projectRequest);
        return ResponseEntity.ok(projectResponse);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long projectId,
                                                    @RequestBody ProjectUpdateDto projectUpdateDto) {
        Project updatedProject = projectService.updateProject(projectId, projectUpdateDto);
        ProjectDTO responseDto = projectService.convertToDto(updatedProject);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    @DeleteMapping("/soft/{id}")
    public ResponseEntity<String> softDeleteProject(@PathVariable Long id) {
            projectService.softDeleteProject(id);
            return ResponseEntity.ok("Project successfully SOFT Delete edildi.");
    }

    @DeleteMapping("/{projectId}/users/{userId}")
    public ResponseEntity<String> removeUserFromProject(@PathVariable Long projectId, @PathVariable Long userId) {
        projectService.removeUserFromProject(projectId, userId);
        return ResponseEntity.ok("User removed from project successfully.");
    }
    @PostMapping("/{projectId}/users/{userId}")
    public ResponseEntity<String> addUserToProject(@PathVariable Long projectId, @PathVariable Long userId) {
        projectService.addUserToProject(projectId, userId);
        return ResponseEntity.ok("User added to project successfully.");
    }

    // Hard Delete
//    @DeleteMapping("/hard/{id}")
//    public ResponseEntity<String> hardDeleteProject(@PathVariable Long id) {
//
//            projectService.hardDeleteProject(id);
//            return ResponseEntity.ok("Project successfully HARD Delete edildi.");
//
//    }
}
