package az.projectdailyreport.projectdailyreport.exception;
public class TeamNotEmptyException extends RuntimeException {
    public TeamNotEmptyException(Long teamId) {
        super("Team with ID " + teamId + " cannot be deleted as it contains users.");
    }
}

