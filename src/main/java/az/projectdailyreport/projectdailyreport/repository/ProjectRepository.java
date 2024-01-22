package az.projectdailyreport.projectdailyreport.repository;

import az.projectdailyreport.projectdailyreport.model.Project;
import az.projectdailyreport.projectdailyreport.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
    boolean existsByProjectName(String projectName);
    boolean existsByIdAndStatus(Long id, Status status);

    @Modifying
    @Query("UPDATE Project p SET p.status = 'DELETED' WHERE p.id = :id")
    void softDeleteProject(@Param("id") Long id);
}
