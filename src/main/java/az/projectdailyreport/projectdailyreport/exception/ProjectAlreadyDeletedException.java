package az.projectdailyreport.projectdailyreport.exception;

public class ProjectAlreadyDeletedException extends RuntimeException {
    public  ProjectAlreadyDeletedException(Long projectId) {
        super("Project with id " + projectId + " is already deleted.");
    }
}
