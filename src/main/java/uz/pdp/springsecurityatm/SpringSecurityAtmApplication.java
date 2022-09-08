package uz.pdp.springsecurityatm;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.pdp.springsecurityatm.entity.*;
import uz.pdp.springsecurityatm.entity.enums.CardName;
import uz.pdp.springsecurityatm.entity.enums.RoleName;
import uz.pdp.springsecurityatm.repository.*;

import java.math.BigDecimal;
import java.util.*;

@SpringBootApplication
public class SpringSecurityAtmApplication implements CommandLineRunner {
    @Value("${spring.data.initialization}")
    private String initialization;
    Calendar calendar = Calendar.getInstance();
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BankRepository bankRepository;
    private final CardRepository cardRepository;
    private final PasswordEncoder passwordEncoder;
    private final CardTypeRepository cardTypeRepository;

    @Autowired
    public SpringSecurityAtmApplication(UserRepository userRepository, RoleRepository roleRepository, BankRepository bankRepository, CardRepository cardRepository, PasswordEncoder passwordEncoder, CardTypeRepository cardTypeRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bankRepository = bankRepository;
        this.cardRepository = cardRepository;
        this.passwordEncoder = passwordEncoder;
        this.cardTypeRepository = cardTypeRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityAtmApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (initialization.equals("always")) {
            rolesSaveDB();
            cardTypeNameSaveDB();
            Bank nbu = bankRepository.save(new Bank(null, "NBU"));
            saveCardWithUser(nbu);
        }
    }

    public void rolesSaveDB() {
        roleRepository.saveAll(
                Arrays.asList(
                        new Role(null, RoleName.ROLE_DIRECTOR),
                        new Role(null, RoleName.ROLE_MANAGER),
                        new Role(null, RoleName.ROLE_USER)
                )
        );
    }

    public void cardTypeNameSaveDB() {
        cardTypeRepository.saveAll(Arrays.asList(
                new CardType(null, CardName.VISA),
                new CardType(null, CardName.UZCARD),
                new CardType(null, CardName.HUMO)
        ));
    }

    @SneakyThrows
    public void saveCardWithUser(Bank bank) {
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, 10);
        Optional<Role> optionalRole = roleRepository.findRoleByRole(RoleName.ROLE_DIRECTOR);
        Optional<CardType> cardTypeByType = cardTypeRepository.findCardTypeByType(CardName.UZCARD);
        if (optionalRole.isPresent() && cardTypeByType.isPresent()) {
            Card card = new Card("8600000000000000", null, passwordEncoder.encode("1234"), bank, BigDecimal.valueOf(10_000_000D), cardTypeByType.get());
            User user = new User("John", "Doe", Collections.singleton(optionalRole.get()));
            card.setEnabled(true);
            user.setCards(Collections.singleton(card));
            BankOwner bankOwner = new BankOwner(null, "darkprohub-uz@yandex.ru", user, bank);
            user.setBankOwner(bankOwner);
            card.setUser(user);
            card.setExpireDate(calendar.getTime());
            Card savedUser = cardRepository.save(card);
            System.out.println("Card and User successfully added:\n" + savedUser);
//            Thread.sleep(30000);
//            userRepository.delete(savedUser.getUser());
//            System.out.println("User muvaffaqiyatli o'chirildi");
        }
    }
}
