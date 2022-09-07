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
    Calendar calendar = Calendar.getInstance();
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
        List<Role> roles = roleRepository.saveAll(Arrays.asList(
                new Role(null, RoleName.ROLE_DIRECTOR),
                new Role(null, RoleName.ROLE_MANAGER),
                new Role(null, RoleName.ROLE_USER)
        ));
        Bank nbu = bankRepository.save(new Bank(null, "NBU"));
//
//        calendar.setTime(new Date());
//        calendar.add(Calendar.YEAR, 10);
//        Optional<Role> roleByRole = roleRepository.findRoleByRole(RoleName.ROLE_DIRECTOR);
//        Card card = new Card("0000999900009999", "123", "1234", nbu, 100D);
//        User user = new User("Jane", "Doe", Collections.singleton(roleByRole.get()));
//        user.setCards(Collections.singleton(card));
//        card.setUser(user);
//        card.setExpireDate(calendar.getTime());
//        cardRepository.save(card);
    }
}
