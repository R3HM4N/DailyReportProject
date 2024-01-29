package az.projectdailyreport.projectdailyreport.exception;
public class UserAlreadyAddedException extends RuntimeException {
    public UserAlreadyAddedException(Long userId, Long projectId) {
        super("User with ID " + userId + " is already added to project with ID " + projectId);
    }
}
