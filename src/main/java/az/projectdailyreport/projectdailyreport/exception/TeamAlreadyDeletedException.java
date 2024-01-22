package az.projectdailyreport.projectdailyreport.exception;
public class TeamAlreadyDeletedException extends RuntimeException {
    public TeamAlreadyDeletedException(Long teamId) {
        super("Team with ID " + teamId + " is already deleted.");
    }
}
