package az.projectdailyreport.projectdailyreport.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)

public class ProjectExistsException extends RuntimeException {
    public ProjectExistsException(String message) {
        super(message);
    }}
