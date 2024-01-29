package az.projectdailyreport.projectdailyreport.exception;
public class UserAlreadyRemovedException extends RuntimeException {
    public UserAlreadyRemovedException(Long userId, Long projectId) {
        super("User with ID " + userId + " is already removed from project with ID " + projectId);
    }
}
