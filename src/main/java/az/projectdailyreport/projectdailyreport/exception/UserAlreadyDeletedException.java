package az.projectdailyreport.projectdailyreport.exception;

public class UserAlreadyDeletedException extends RuntimeException {
    public UserAlreadyDeletedException(Long userId) {
        super("User with ID " + userId + " is already deleted.");
    }

}
