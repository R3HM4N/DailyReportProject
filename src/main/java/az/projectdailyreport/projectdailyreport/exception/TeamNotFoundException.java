package az.projectdailyreport.projectdailyreport.exception;
public class TeamNotFoundException extends RuntimeException {
    public  TeamNotFoundException(Long teamId) {
        super("Team with ID " + teamId + " not found.");
    }
}
