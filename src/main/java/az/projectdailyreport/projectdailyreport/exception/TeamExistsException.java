package az.projectdailyreport.projectdailyreport.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class TeamExistsException extends RuntimeException {
    public TeamExistsException(String message) {
        super(message);
    }}
