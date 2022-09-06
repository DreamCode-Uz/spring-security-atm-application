package uz.pdp.springsecurityatm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uz.pdp.springsecurityatm.entity.Bank;
import uz.pdp.springsecurityatm.entity.Role;
import uz.pdp.springsecurityatm.entity.enums.RoleName;
import uz.pdp.springsecurityatm.repository.BankRepository;
import uz.pdp.springsecurityatm.repository.CardRepository;
import uz.pdp.springsecurityatm.repository.RoleRepository;
import uz.pdp.springsecurityatm.repository.UserRepository;

import java.util.Arrays;

@SpringBootApplication
public class SpringSecurityAtmApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BankRepository bankRepository;
    private final CardRepository cardRepository;

    @Autowired
    public SpringSecurityAtmApplication(UserRepository userRepository, RoleRepository roleRepository, BankRepository bankRepository, CardRepository cardRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bankRepository = bankRepository;
        this.cardRepository = cardRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityAtmApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        roleRepository.saveAll(Arrays.asList(
                new Role(null, RoleName.ROLE_DIRECTOR),
                new Role(null, RoleName.ROLE_MANAGER),
                new Role(null, RoleName.ROLE_USER)
        ));
        bankRepository.saveAll(
                Arrays.asList(
                        new Bank(null, "NBU"),
                        new Bank(null, "QQB")
                )
        );

    }
}
