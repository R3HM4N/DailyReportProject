package az.projectdailyreport.projectdailyreport.exception;

public class ProjectNotFoundException extends  RuntimeException {
    private final Long projectId;

    public ProjectNotFoundException(Long projectId) {
        super("Project not found with ID: " + projectId);
        this.projectId = projectId;
    }

    public Long getProjectId() {
        return projectId;
    }
}
