package az.projectdailyreport.projectdailyreport.controller;

import az.projectdailyreport.projectdailyreport.dto.request.CreateUserRequest;
import az.projectdailyreport.projectdailyreport.model.Status;
import az.projectdailyreport.projectdailyreport.model.User;
import az.projectdailyreport.projectdailyreport.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        User createdUser = userService.createUser(createUserRequest);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
//    @DeleteMapping("/softdelete/{userId}")
//    public ResponseEntity<String> softDeleteUser(@PathVariable Long userId) {
//        userService.softDeleteUser(userId);
//        return new ResponseEntity<>("User soft deleted successfully", HttpStatus.OK);
//    }
@GetMapping("/filter")
public List<User> getUsersWithFilters(@RequestParam(required = false) Status status,
                                      @RequestParam(required = false) String firstName,
                                      @RequestParam(required = false) String lastName,
                                      @RequestParam(required = false) List<Long> projectIds) {
    return userService.getUsersWithFilters(status, firstName, lastName, projectIds);
}
    @DeleteMapping("/harddelete/{userId}")
    public ResponseEntity<String> hardDeleteUser(@PathVariable Long userId) {
        userService.hardDeleteUser(userId);
        return ResponseEntity.ok("User successfully hard delete edildi.");
    }
}
