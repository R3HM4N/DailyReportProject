package az.projectdailyreport.projectdailyreport.repository;

import az.projectdailyreport.projectdailyreport.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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
    boolean existsByRole(Role role);
   Optional< User>  findByResetToken(String resetToken);

    Optional<User> findByChangeIsTrue();




    @EntityGraph(attributePaths = "projects")
    Optional<User> findById(Long id);



    //    @Query("SELECT DISTINCT u FROM User u JOIN u.projects p " +
    //            "WHERE (:firstName IS NULL OR u.firstName LIKE %:firstName%) AND " +
    //            "(:lastName IS NULL OR u.lastName LIKE %:lastName%) AND " +
    //            "(:status IS NULL OR u.status = :status) AND " +
    //            "(:teamId IS NULL OR u.team.id = :teamId) AND " +
    //            "(:projectIds IS NULL OR (:projectIds IS NOT NULL AND p.id IN :projectIds))")
    //    Page<User> findByFilters(
    //            @Param("firstName") String firstName,
    //            @Param("lastName") String lastName,
    //            @Param("status") Status status,
    //            @Param("teamId") Long teamId,
    //            @Param("projectIds") List<Long> projectIds,
    //            Pageable pageable
    //    );
    @Query("SELECT u FROM User u " +
            "WHERE u.deleted = false " + // Exclude deleted users
            "AND (:firstName IS NULL OR u.firstName LIKE %:firstName%) " +
            "AND (:lastName IS NULL OR u.lastName LIKE %:lastName%) " +
            "AND (:status IS NULL OR u.status = :status) " +
            "AND (:teamIds IS NULL OR u.team.id IN :teamIds) " +
            "AND (:projectIds IS NULL OR EXISTS (SELECT p FROM u.projects p WHERE p.id IN :projectIds))")
    Page<User> findByFilters(String firstName, String lastName, Status status, List<Long> teamIds, List<Long> projectIds, Pageable pageable);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.projects WHERE u.id = :userId")
    Optional<User> findByIdWithProjects(@Param("userId") Long userId);
    @Query("SELECT u FROM User u WHERE u.mail = :email")
    Optional<User> findByEmail(@Param("email") String email);
    boolean existsByMail(String mail);




}


