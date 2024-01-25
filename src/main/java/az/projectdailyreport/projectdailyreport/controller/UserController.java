package az.projectdailyreport.projectdailyreport.controller;

import az.projectdailyreport.projectdailyreport.dto.UserDTO;
import az.projectdailyreport.projectdailyreport.dto.UserGetDTO;
import az.projectdailyreport.projectdailyreport.dto.request.CreateUserRequest;
import az.projectdailyreport.projectdailyreport.model.Status;
import az.projectdailyreport.projectdailyreport.model.User;
import az.projectdailyreport.projectdailyreport.service.UserService;
import az.projectdailyreport.projectdailyreport.unit.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @DeleteMapping("/harddelete/{userId}")
    public ResponseEntity<String> hardDeleteUser(@PathVariable Long userId) {
        userService.hardDeleteUser(userId);
        return ResponseEntity.ok("User successfully hard delete edildi.");
    }
    @GetMapping("/filters")
    public ResponseEntity<Page<UserDTO>> getUsersByFilters(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Long teamId,
            @RequestParam(required = false) List<Long> projectIds,
            @RequestParam(name = "pageNumber", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "ASC") String sortOrder) {

        // Pageable oluşturma
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortOrder), sortBy);

        // UserService'den sayfalama ile kullanıcıları alma
        Page<UserDTO> users = userService.getUsersByFilters(firstName, lastName, status, teamId, projectIds, pageable);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserGetDTO> getUserById(@PathVariable Long userId) {
        UserGetDTO userDTO = userService.getUserById(userId);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }


}


