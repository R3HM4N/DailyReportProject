package az.projectdailyreport.projectdailyreport.service;

import az.projectdailyreport.projectdailyreport.dto.*;
import az.projectdailyreport.projectdailyreport.dto.request.ConfirmPassword;
import az.projectdailyreport.projectdailyreport.dto.request.CreateUserRequest;
import az.projectdailyreport.projectdailyreport.dto.request.UserResetPasswordRequest;
import az.projectdailyreport.projectdailyreport.model.Status;
import az.projectdailyreport.projectdailyreport.model.User;
import az.projectdailyreport.projectdailyreport.unit.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface UserService {
    User createUser(CreateUserRequest createUserRequest);
    List<UserGetAll> getAll();
    User getSignedInUser();
    String resetPassword(Long userId, UserReset userReset);
    void confirmPassword(ConfirmPassword confirmPassword);
    void changePassword(UserChangePassword changePassword);
    void resetPasswordWithOtp(UserResetPasswordRequest forgetDto);
    void sendPasswordResetEmail(String email);

    void softDeleteUser(Long userId);
    List<User> getUsersByIds(List<Long> userIds);
    UserDTO updateUser(Long userId, UserUpdateDTO updatedUserDTO);


    void changeUserStatus(Long userId, Status newStatus);
    Page<UserDTO> getUsersByFilters(String firstName, String lastName, Status status, List<Long> teamId, List<Long> projectIds, Pageable pageable);    UserGetDTO getUserById(Long userId);


}


