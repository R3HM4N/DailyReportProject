package az.projectdailyreport.projectdailyreport.repository;

import az.projectdailyreport.projectdailyreport.model.Role;
import az.projectdailyreport.projectdailyreport.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByRoleName(String roleName);
    boolean existsByRoleName(RoleName roleName);

}
