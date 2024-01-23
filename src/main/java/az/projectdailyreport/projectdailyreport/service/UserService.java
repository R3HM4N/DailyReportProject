package az.projectdailyreport.projectdailyreport.service;

import az.projectdailyreport.projectdailyreport.dto.request.CreateUserRequest;
import az.projectdailyreport.projectdailyreport.model.Status;
import az.projectdailyreport.projectdailyreport.model.User;

import java.util.List;

public interface UserService {
    User createUser(CreateUserRequest createUserRequest);
//    void softDeleteUser(Long userId);
    void hardDeleteUser(Long userId);
    List<User> getUsersWithFilters(Status status, String firstName, String lastName, List<Long> projectIds);


}

