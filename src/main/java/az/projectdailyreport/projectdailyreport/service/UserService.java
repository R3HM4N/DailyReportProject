package az.projectdailyreport.projectdailyreport.service;

import az.projectdailyreport.projectdailyreport.dto.UserDTO;
import az.projectdailyreport.projectdailyreport.dto.UserGetDTO;
import az.projectdailyreport.projectdailyreport.dto.request.CreateUserRequest;
import az.projectdailyreport.projectdailyreport.model.Status;
import az.projectdailyreport.projectdailyreport.model.User;
import az.projectdailyreport.projectdailyreport.unit.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface UserService {
    User createUser(CreateUserRequest createUserRequest);
//    void softDeleteUser(Long userId);
    void hardDeleteUser(Long userId);
    List<User> getUsersByIds(List<Long> userIds);



    Page<UserDTO> getUsersByFilters(String firstName, String lastName, Status status, Long teamId, List<Long> projectIds, Pageable pageable);
    UserGetDTO getUserById(Long userId);

        //@Query("SELECT DISTINCT u FROM User u JOIN u.projects p " +
    //           "WHERE (:firstName IS NULL OR u.firstName = :firstName) AND " +
    //           "(:lastName IS NULL OR u.lastName = :lastName) AND " +
    //           "(:status IS NULL OR u.status = :status) AND " +
    //           "(:projectId IS NULL OR p.id = :projectId)")
    //    List<User> findByFilters(
    //        @Param("firstName") String firstName,
    //        @Param("lastName") String lastName,
    //        @Param("status") String status,
    //        @Param("projectId") Long projectId);
}


