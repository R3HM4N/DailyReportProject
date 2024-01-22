package az.projectdailyreport.projectdailyreport.repository;

import az.projectdailyreport.projectdailyreport.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query("UPDATE User u SET u.status = 'DELETED' WHERE u.id = :userId")
    void softDeleteUser(@Param("userId") Long userId);

}
