package az.projectdailyreport.projectdailyreport.repository;

import az.projectdailyreport.projectdailyreport.model.Project;
import az.projectdailyreport.projectdailyreport.model.Status;
import az.projectdailyreport.projectdailyreport.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query("UPDATE User u SET u.status = 'DELETED' WHERE u.id = :userId")
    void softDeleteUser(@Param("userId") Long userId);



    @Query("SELECT DISTINCT u FROM User u JOIN u.projects p " +
            "WHERE (:firstName IS NULL OR u.firstName LIKE %:firstName%) AND " +
            "(:lastName IS NULL OR u.lastName LIKE %:lastName%) AND " +
            "(:status IS NULL OR u.status = :status) AND " +
            "(:teamId IS NULL OR u.team.id = :teamId) AND " +
            "(:projectIds IS NULL OR p.id IN :projectIds)")
    Page<User> findByFilters(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("status") Status status,
            @Param("teamId") Long teamId,
            @Param("projectIds") List<Long> projectIds,
            Pageable pageable
    );
    Optional<User> findById(Long id);



}


