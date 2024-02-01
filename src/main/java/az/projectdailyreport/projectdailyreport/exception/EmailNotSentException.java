package az.projectdailyreport.projectdailyreport.exception;

public class EmailNotSentException extends RuntimeException {
    public EmailNotSentException(String message) {
        super(message);
    }
}
