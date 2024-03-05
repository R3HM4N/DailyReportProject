package az.projectdailyreport.projectdailyreport.repository;

import az.projectdailyreport.projectdailyreport.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team,Long> {


    @Modifying
    @Query("UPDATE Team t SET t.status = 'DELETED' WHERE t.id = :id")
    void softDeleteTeam(@Param("id") Long id);

    boolean existsByTeamName(String teamName);
    Optional<Team> findByTeamNameIgnoreCase(String teamName);
}
