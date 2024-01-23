package az.projectdailyreport.projectdailyreport.repository;

import az.projectdailyreport.projectdailyreport.model.Project;
import az.projectdailyreport.projectdailyreport.model.Status;
import az.projectdailyreport.projectdailyreport.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query("UPDATE User u SET u.status = 'DELETED' WHERE u.id = :userId")
    void softDeleteUser(@Param("userId") Long userId);

    @Query("SELECT u FROM User u WHERE " +
            "(:status IS NULL OR u.status = :status) AND " +
            "(:name IS NULL OR u.firstName = :name) AND " +
            "(:surname IS NULL OR u.lastName = :surname) AND " +
            "(:projects IS NULL OR NOT EXISTS (SELECT p FROM Project p WHERE p IN :projects AND p MEMBER OF u.projects))")
    List<User> findAllWithFilters(
            @Param("status") Status status,
            @Param("name") String firstName,
            @Param("surname") String lastName,
            @Param("projects") List<Project> projects
    );}

