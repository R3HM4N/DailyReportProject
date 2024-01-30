package az.projectdailyreport.projectdailyreport.unit;

import az.projectdailyreport.projectdailyreport.model.RoleName;
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
        // Super_ADMIN kullanıcısı daha önce eklenmediyse ekle
        if (!userRepository.existsByRole(roleRepository.findById(1L).get())) {
            User superAdmin =User.builder()
                    .mail("superadmin@crocusoft.com")
                    .password(passwordEncoder.encode("superadminpassword"))
                    .role(roleRepository.findById(1L).get())
                    .roleName(RoleName.SUPER_ADMIN)
                    .build();

            userRepository.save(superAdmin);
            System.out.println("Super_ADMIN kullanıcısı başarıyla oluşturuldu.");
        } else {
            System.out.println("Super_ADMIN kullanıcısı zaten var.");
        }
    }
}
