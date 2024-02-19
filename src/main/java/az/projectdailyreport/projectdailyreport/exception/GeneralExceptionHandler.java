package az.projectdailyreport.projectdailyreport.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(MailValidationException.class)
    public ResponseEntity<ExceptionResponse> handleMailValidation(MailValidationException exception) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST,
                exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProjectExistsException.class)
    public ResponseEntity<ExceptionResponse> handleProjectExistsException(ProjectExistsException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                LocalDateTime.now(),
                CONFLICT.value(),
                CONFLICT,
                exception.getMessage());
        return new ResponseEntity<>(exceptionResponse, CONFLICT);
    }
    @ExceptionHandler(TeamExistsException.class)
    public ResponseEntity<ExceptionResponse> handleTeamExistsException(TeamExistsException exception) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                CONFLICT.value(),
                CONFLICT,
                exception.getMessage());

        return new ResponseEntity<>(response, CONFLICT);}

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleProjectNotFoundException(ProjectNotFoundException exception) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                NOT_FOUND.value(),
                NOT_FOUND,
                exception.getMessage());

        return new ResponseEntity<>(response, NOT_FOUND);
    }
    @ExceptionHandler(ProjectAlreadyDeletedException.class)
    public ResponseEntity<ExceptionResponse> handleProjectAlreadyDeletedException(ProjectAlreadyDeletedException exception) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST,
                exception.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }


    @ExceptionHandler(UserAlreadyDeletedException.class)
    public ResponseEntity<ExceptionResponse> handleUserAlreadyDeletedException(UserAlreadyDeletedException exception) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST,
                exception.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException exception) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST,
                exception.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleTeamNotFoundException(TeamNotFoundException exception) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST,
                exception.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(TeamAlreadyDeletedException.class)
    public ResponseEntity<ExceptionResponse> handleTeamAlreadyDeletedException(TeamAlreadyDeletedException exception) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST,
                exception.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateReportException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateReportException(DuplicateReportException exception) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST,
                exception.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(ReportNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleReportNotFoundException (ReportNotFoundException  exception) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST,
                exception.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }
    @ExceptionHandler(DailyReportUpdateException.class)
    public ResponseEntity<ExceptionResponse> handleDailyReportUpdateException  (DailyReportUpdateException   exception) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST,
                exception.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyRemovedException.class)
    public  ResponseEntity<ExceptionResponse> handleUserAlreadyRemovedException  (UserAlreadyRemovedException   exception) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST,
                exception.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyAddedException.class)
    public ResponseEntity<ExceptionResponse> handleUserAlreadyAddedException  (UserAlreadyAddedException   exception) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST,
                exception.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(TeamNotEmptyException.class)
    public  ResponseEntity<ExceptionResponse> handleTeamNotEmptyException (TeamNotEmptyException  exception){
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST,
                exception.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);

    }

    @ExceptionHandler(MailAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleMailAlreadyExistsException (MailAlreadyExistsException  exception){
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST,
                exception.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);

    }


    @ExceptionHandler(SuperAdminException.class)
    public   ResponseEntity<ExceptionResponse> handleSuperAdminException (SuperAdminException  exception){
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST,
                exception.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);

    }


    @ExceptionHandler(RoleException.class)
    public  ResponseEntity<ExceptionResponse>  handleRoleException (RoleException  exception){
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST,
                exception.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);

    }



    @ExceptionHandler(InvalidPasswordException.class)
    public  ResponseEntity<ExceptionResponse> handleInvalidPasswordException (InvalidPasswordException  exception){
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST,
                exception.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);

    }

    @ExceptionHandler(EmailNotSentException.class)
    public  ResponseEntity<ExceptionResponse> handleEmailNotSentException (EmailNotSentException  exception){
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST,
                exception.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);

    }

    @ExceptionHandler(InvalidOtpException.class)
    public  ResponseEntity<ExceptionResponse> handleInvalidOtpException (InvalidOtpException  exception){
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST,
                exception.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);

    }


    @ExceptionHandler(PasswordsNotMatchException.class)
    public  ResponseEntity<ExceptionResponse> handlePasswordsNotMatchException (PasswordsNotMatchException  exception){
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST,
                exception.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);


    }

}
