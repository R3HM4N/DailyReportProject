package az.projectdailyreport.projectdailyreport.repository;

import az.projectdailyreport.projectdailyreport.model.Deleted;
import az.projectdailyreport.projectdailyreport.model.Project;
import az.projectdailyreport.projectdailyreport.model.Status;
import az.projectdailyreport.projectdailyreport.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
    boolean existsByProjectName(String projectName);
    boolean existsByIdAndStatus(Long id, Deleted deleted);
    Optional<Project> findById(Long projectId);
    Page<Project> findByProjectNameContainingIgnoreCase(String projectName, Pageable pageable);

    @Query("SELECT p FROM Project p WHERE LOWER(p.projectName) LIKE LOWER(CONCAT('%', :projectName, '%'))")
    Page<Project> findByProjectNameContaining(@Param("projectName") String projectName, Pageable pageable);


    Optional<Project> findByProjectNameIgnoreCase(String projectName);

    @Modifying
    @Query("UPDATE Project p SET p.status = 'DELETED' WHERE p.id = :id")
    void softDeleteProject(@Param("id") Long id);
}
