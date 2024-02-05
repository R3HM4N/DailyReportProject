package az.projectdailyreport.projectdailyreport.controller;

import az.projectdailyreport.projectdailyreport.dto.project.*;
import az.projectdailyreport.projectdailyreport.dto.request.ProjectRequest;
import az.projectdailyreport.projectdailyreport.model.Project;
import az.projectdailyreport.projectdailyreport.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<String> updateProjectAndUsers(@PathVariable Long projectId,
                                                         @RequestBody ProjectUpdateDto projectUpdateDto,
                                                         @RequestParam(required = false) List<Long> userIdsToAdd,
                                                         @RequestParam(required = false) List<Long> userIdsToRemove) {
        Project updatedProject = projectService.updateProjectAndUsers(projectId, projectUpdateDto, userIdsToAdd, userIdsToRemove);
        return ResponseEntity.ok("Project successfully Updated .");
    }
    @GetMapping("/search")
    public ResponseEntity<Page<ProjectFilterDto>> searchProjectsByName(
            @RequestParam(value = "name",required = false) String projectName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectFilterDto> filteredProjects = projectService.searchProjectsByName(projectName, pageable);
        if (filteredProjects.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(filteredProjects);
    }

}
