package az.projectdailyreport.projectdailyreport.controller;

import az.projectdailyreport.projectdailyreport.dto.*;
import az.projectdailyreport.projectdailyreport.dto.request.ConfirmPassword;
import az.projectdailyreport.projectdailyreport.dto.request.CreateUserRequest;
import az.projectdailyreport.projectdailyreport.dto.request.UserResetPasswordRequest;
import az.projectdailyreport.projectdailyreport.model.Status;
import az.projectdailyreport.projectdailyreport.model.User;
import az.projectdailyreport.projectdailyreport.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserGetAll>> getAllUsers() {
        List<UserGetAll> users = userService.getAll();
        return ResponseEntity.ok(users);
    }


    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        User createdUser = userService.createUser(createUserRequest);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<String> softDeleteUser(@PathVariable Long userId) {
        userService.softDeleteUser(userId);
        return new ResponseEntity<>("User soft deleted successfully", HttpStatus.OK);
    }


    @GetMapping("/filters")
    public ResponseEntity<Page<UserDTO>> getUsersByFilters(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) List<Long> teamId,
            @RequestParam(required = false) List<Long> projectIds,
            @RequestParam(name = "pageNumber", defaultValue = "1") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "ASC") String sortOrder) {

        Pageable pageable = PageRequest.of(page-1, size, Sort.Direction.fromString(sortOrder), sortBy);

        Page<UserDTO> users = userService.getUsersByFilters(firstName, lastName, status, teamId, projectIds, pageable);
        return ResponseEntity.ok(users);

    }
    @GetMapping("/{userId}")
    public ResponseEntity<UserGetDTO> getUserById(@PathVariable Long userId) {
        UserGetDTO userDTO = userService.getUserById(userId);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);}

    @GetMapping("/status/list")
    public List<String> getStatusList() {
        return Arrays.stream(Status.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody UserUpdateDTO updatedUserDTO) {
        UserDTO updatedUser = userService.updateUser(userId, updatedUserDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);

    }
    @PutMapping("/{userId}/status")
    public ResponseEntity<String> changeUserStatus(@PathVariable Long userId, @RequestParam Status newStatus) {
        userService.changeUserStatus(userId, newStatus);
        return ResponseEntity.ok("User status has been updated successfully");
    }
    @PutMapping("/{userId}/reset-password")
    public String resetPassword(@PathVariable Long userId, @RequestBody UserReset userReset) {
        return userService.resetPassword(userId, userReset);
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody UserChangePassword changePassword) {

            userService.changePassword(changePassword);
            return ResponseEntity.ok("Password changed successfully");


    }

    @PostMapping("/forget-password-email")
    public ResponseEntity<String> forgetSendPasswordResetEmail(@RequestParam String email) {
        userService.sendPasswordResetEmail(email);
        return new ResponseEntity<>("Password reset email sent successfully", HttpStatus.OK);
    }

    @PostMapping("/forget-password-otp")
    public ResponseEntity<String> forgetResetPasswordWithOtp(
            @RequestBody UserResetPasswordRequest resetPasswordRequest) {
        userService.resetPasswordWithOtp(resetPasswordRequest );
        return new ResponseEntity<>("Otp code correct", HttpStatus.OK);
    }
    @PutMapping("/confirm-password")
    public void confirmPassword(@RequestParam String mail ,@RequestBody ConfirmPassword confirmPassword) {
        userService.confirmPassword(mail,confirmPassword);
    }
    @GetMapping("/profile")
    public ResponseEntity<UserGetDTO> getLoginUserById() {
        User user =userService.getSignedInUser();
        UserGetDTO userDTO = userService.getUserById(user.getId());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);}




}