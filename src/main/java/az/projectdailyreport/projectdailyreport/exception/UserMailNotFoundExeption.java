package az.projectdailyreport.projectdailyreport.exception;

public class UserMailNotFoundExeption extends RuntimeException {
    public UserMailNotFoundExeption(String  mail) {
        super("User with mail " + mail + " not found.");
    }}
