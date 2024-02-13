package az.projectdailyreport.projectdailyreport.unit;

import az.projectdailyreport.projectdailyreport.model.Role;
import az.projectdailyreport.projectdailyreport.model.RoleName;
import az.projectdailyreport.projectdailyreport.model.Status;
import az.projectdailyreport.projectdailyreport.model.User;
import az.projectdailyreport.projectdailyreport.repository.RoleRepository;
import az.projectdailyreport.projectdailyreport.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component

public class SuperAdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    @Autowired
    private  RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public SuperAdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args) throws Exception {
        for (RoleName roleName : RoleName.values()) {
            if (!roleRepository.existsByRoleName(roleName)) {
                Role role = new Role();
                role.setRoleName(roleName);
                roleRepository.save(role);
            }
        }
        if (!userRepository.existsByRole(roleRepository.findById(3L).get())){
            User head = User.builder()
                    .firstName("boyuk")
                    .lastName("boyuk")
                    .mail("head@crocusoft.com")
                    .password(passwordEncoder.encode("BoyukHead"))
                    .status(Status.ACTIVE)
                    .role(roleRepository.findById(3L).get())
                    .roleName(RoleName.HEAD)
            .build();
            userRepository.save(head);
            System.out.println("Head created");

        }
        else {
            System.out.println("Head already created");
        }
        if (!userRepository.existsByRole(roleRepository.findById(1L).get())) {
            User superAdmin =User.builder()
                    .mail("superadmin@crocusoft.com")
                    .firstName("Rehman")
                    .lastName("SuperAdmin")
                    .password(passwordEncoder.encode("superadminpassword"))
                    .status(Status.ACTIVE)
                    .role(roleRepository.findById(1L).get())
                    .roleName(RoleName.SUPER_ADMIN)
                    .build();

            userRepository.save(superAdmin);
            System.out.println("Super_ADMIN created.");
        } else {
            System.out.println("Super_ADMIN already created.");
        }
    }
}
