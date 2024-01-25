package az.projectdailyreport.projectdailyreport.controller;

import az.projectdailyreport.projectdailyreport.dto.ProjectDTO;
import az.projectdailyreport.projectdailyreport.dto.ProjectResponse;
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
    public List<Project> getAllProjects() {
        return projectService.getAllProject();
    }
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest projectRequest) {
        ProjectResponse projectResponse = projectService.createProject(projectRequest);
        return ResponseEntity.ok(projectResponse);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long projectId,
                                                         @RequestBody ProjectRequest updatedProjectRequest) {
        Project updatedProject = projectService.updateProject(projectId, updatedProjectRequest);
        ProjectResponse responseDto = convertToDto(updatedProject);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    private ProjectResponse convertToDto(Project project) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(project, ProjectResponse.class);
    }
    @DeleteMapping("/soft/{id}")
    public ResponseEntity<String> softDeleteProject(@PathVariable Long id) {
            projectService.softDeleteProject(id);
            return ResponseEntity.ok("Project successfully SOFT Delete edildi.");
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
