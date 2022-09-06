package uz.pdp.springsecurityatm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.pdp.springsecurityatm.entity.Bank;
import uz.pdp.springsecurityatm.entity.Card;
import uz.pdp.springsecurityatm.entity.Role;
import uz.pdp.springsecurityatm.entity.User;
import uz.pdp.springsecurityatm.entity.enums.RoleName;
import uz.pdp.springsecurityatm.repository.BankRepository;
import uz.pdp.springsecurityatm.repository.CardRepository;
import uz.pdp.springsecurityatm.repository.RoleRepository;
import uz.pdp.springsecurityatm.repository.UserRepository;

import java.util.*;

@SpringBootApplication
public class SpringSecurityAtmApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BankRepository bankRepository;
    private final CardRepository cardRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SpringSecurityAtmApplication(UserRepository userRepository, RoleRepository roleRepository, BankRepository bankRepository, CardRepository cardRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bankRepository = bankRepository;
        this.cardRepository = cardRepository;
        this.passwordEncoder = passwordEncoder;
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
        List<Bank> banks = bankRepository.saveAll(
                Arrays.asList(
                        new Bank(null, "NBU"),
                        new Bank(null, "QQB")
                )
        );
        Optional<Role> director = roleRepository.findRoleByRole(RoleName.ROLE_DIRECTOR);
        User savedUser = userRepository.save(new User(null, "John", "Doe", Collections.singleton(director.get())));
        Card card = new Card("8600111122223333", null, passwordEncoder.encode("1234"), banks.get(0), savedUser, 0D);
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        instance.add(Calendar.YEAR, 10);
        card.setExpireDate(instance.getTime());
        card.setEnabled(true);
        cardRepository.save(card);
    }
}
