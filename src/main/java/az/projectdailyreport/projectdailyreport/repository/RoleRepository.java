package az.projectdailyreport.projectdailyreport.repository;

import az.projectdailyreport.projectdailyreport.model.Role;
import az.projectdailyreport.projectdailyreport.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByRoleName(String roleName);
    @Query("SELECT r FROM Role r WHERE r.id != 1")
    List<Role> findAllExceptOne();
    boolean existsByRoleName(RoleName roleName);

    @Query("SELECT r FROM Role r WHERE r.roleName NOT IN (:excludedRoles)")
    List<Role> findAllExceptRoles(@Param("excludedRoles") List<String> excludedRoles);
}

