package az.projectdailyreport.projectdailyreport.service;

import az.projectdailyreport.projectdailyreport.dto.request.CreateUserRequest;
import az.projectdailyreport.projectdailyreport.model.User;

public interface UserService {
    User createUser(CreateUserRequest createUserRequest);
}

