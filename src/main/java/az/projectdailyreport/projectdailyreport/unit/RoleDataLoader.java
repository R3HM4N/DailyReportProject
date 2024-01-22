package az.projectdailyreport.projectdailyreport.unit;
import az.projectdailyreport.projectdailyreport.model.Role;
import az.projectdailyreport.projectdailyreport.model.RoleName;
import az.projectdailyreport.projectdailyreport.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleDataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;



    @Override
    public void run(String... args) throws Exception {
        // Enum içindeki rolleri alarak veritabanına kaydet
        for (RoleName roleName : RoleName.values()) {
            if (!roleRepository.existsByRoleName(roleName)) {
                Role role = new Role();
                role.setRoleName(roleName);
                roleRepository.save(role);
            }
        }
    }
}

