package az.projectdailyreport.projectdailyreport.exception;

public class MailAlreadyExistsException extends RuntimeException {
    public MailAlreadyExistsException(String message) {
        super(message);
    }
}
