package uz.pdp.springsecurityatm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurityatm.entity.*;
import uz.pdp.springsecurityatm.entity.enums.CardName;
import uz.pdp.springsecurityatm.entity.enums.RoleName;
import uz.pdp.springsecurityatm.payload.CardDTO;
import uz.pdp.springsecurityatm.payload.CardManagerDTO;
import uz.pdp.springsecurityatm.payload.action.Action;
import uz.pdp.springsecurityatm.payload.response.CardManagerResponse;
import uz.pdp.springsecurityatm.payload.response.CardResponse;
import uz.pdp.springsecurityatm.repository.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.*;

@Service
public class CardService {
    private final CardRepository repository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BankRepository bankRepository;
    private final CardTypeRepository typeRepository;
    private final PasswordEncoder passwordEncoder;
    private final BankOwnerRepository ownerRepository;
    private final MailService mailService;

    @Autowired
    public CardService(CardRepository repository, UserRepository userRepository, RoleRepository roleRepository, BankRepository bankRepository, CardTypeRepository typeRepository, PasswordEncoder passwordEncoder, BankOwnerRepository ownerRepository, MailService mailService) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bankRepository = bankRepository;
        this.typeRepository = typeRepository;
        this.passwordEncoder = passwordEncoder;
        this.ownerRepository = ownerRepository;
        this.mailService = mailService;
    }

    public ResponseEntity<?> getAllCards(Integer page, Integer size) {
        Page<CardResponse> all = repository.findAll(PageRequest.of(page > 0 ? page - 1 : 0, size > 0 ? size : 10)).map(CardResponse::new);
        return ok(all);
    }

    //    BITTA KARTANI MA'LUMOTLARINI QAYTARISH
    public ResponseEntity<?> getCard(UUID id) {
        Optional<Card> optionalCard = repository.findById(id);
        return status(optionalCard.isPresent() ? OK : NOT_FOUND).body(optionalCard.isPresent() ? new CardResponse(optionalCard.get()) : "Card not found");
    }

    //    MAVJUD BARCHA KARTA TOIFALARINI QAYTARISH
    public ResponseEntity<?> getAllCardTypes() {
        return ok(typeRepository.findAll());
    }

    //    MANAGER BANK KARTASINI YARATISHI MUMKIN
    public ResponseEntity<?> registerCard(CardDTO dto) {
        Optional<CardType> cardType = typeRepository.findById(dto.getTypeId());
        if (!cardType.isPresent()) return status(NOT_FOUND).body("Card type not found");
        Optional<Bank> optionalBank = bankRepository.findById(dto.getBankId());
        if (!optionalBank.isPresent()) return status(NOT_FOUND).body("Bank not found");
        String code = Action.generateCardCode(cardType.get().getType());
        while (repository.existsByCardNumber(code)) {
            code = Action.generateCardCode(cardType.get().getType());
        }
        String cvv = null;
        if (cardType.get().getType().equals(CardName.VISA))
            cvv = String.valueOf((int) (Math.random() * (999 - 100) + 100));
        Card card = new Card(code, cvv, passwordEncoder.encode(dto.getPinCode()), optionalBank.get(), BigDecimal.valueOf(dto.getBalance()), cardType.get());
        Optional<Role> roleByRole = roleRepository.findRoleByRole(RoleName.ROLE_USER);
        if (!roleByRole.isPresent()) return badRequest().body("Role bo'yicha mummo dasturchiga murojat qiling");
        User user = new User(dto.getFirstname(), dto.getLastname(), Collections.singleton(roleByRole.get()));
        user.setCards(Collections.singleton(card));
        card.setEnabled(true);
        card.setUser(user);
        card.setExpireDate(Action.getCustomExpireDate(10));
        return status(CREATED).body(new CardResponse(repository.save(card)));
    }

    //    DIRECTOR MANAGERNI VA KARTANI YARATADI
    public ResponseEntity<?> registerManager(CardManagerDTO dto) {
        if (ownerRepository.existsByEmail(dto.getEmail())) return status(CONFLICT).body("Email already registered");
        Optional<CardType> optionalCardType = typeRepository.findById(dto.getTypeId());
        if (!optionalCardType.isPresent()) return status(NOT_FOUND).body("Card type not found");
        Optional<Bank> optionalBank = bankRepository.findById(dto.getBankId());
        if (!optionalBank.isPresent()) return status(NOT_FOUND).body("Bank not found");
        Optional<Role> roleByRole = roleRepository.findRoleByRole(RoleName.ROLE_MANAGER);
        if (!roleByRole.isPresent()) return status(NOT_FOUND).body("ROLE_MANAGER not found");
        CardType cardType = optionalCardType.get();
        String cardNumber = Action.generateCardCode(cardType.getType());
        while (repository.existsByCardNumber(cardNumber)) {
            cardNumber = Action.generateCardCode(cardType.getType());
        }
        String cvv = null;
        if (cardType.getType().equals(CardName.VISA))
            cvv = String.valueOf((int) (Math.random() * (999 - 100) + 100));
        Card card = new Card(cardNumber, cvv, passwordEncoder.encode(dto.getPinCode()), optionalBank.get(), BigDecimal.valueOf(dto.getBalance()), cardType);
        User user = new User(dto.getFirstname(), dto.getLastname(), Collections.singleton(roleByRole.get()));
        user.setCards(Collections.singleton(card));
        user.setBankOwner(new BankOwner(null, dto.getEmail(), user, optionalBank.get()));
        card.setUser(user);
        card.setExpireDate(Action.getCustomExpireDate(10));
        boolean isSendMail = mailService.sendMail(dto.getEmail(), "Accountni activlashtirish",
                String.format("http://localhost:8081/api/auth/verify?code=%s&mail=%s", card.getId(), dto.getEmail()));
        System.out.println("Send Mail: " + isSendMail);
        return ok(new CardManagerResponse(repository.save(card)));
    }

    //    MAVJUD KARTANI MUDDATINI UZAYTIRISH YANI MAVJUD KARTA IDISI KIRITILGANDA SHU KARTANI YANA {amount} YILGA UZAYTIRISH
    public ResponseEntity<?> renewalOfValidity(UUID cardId, Integer amount) {
        Optional<Card> optionalCard = repository.findById(cardId);
        if (!optionalCard.isPresent()) return status(NOT_FOUND).body("Card not found");
        Card card = optionalCard.get();
//        MINIMUM 5 YIL QAYTARADI
        card.setExpireDate(Action.getCustomExpireDate(amount));
        return ok(new CardResponse(repository.save(card)));
    }

    //    MAVJUD KARTANI ACTIVATE YOKI DEACTIVATE QILISH(faol yoki nofaol)
    public ResponseEntity<?> activateOrDeactivateTheCard(UUID cardId, boolean isActive) {
        Optional<Card> optionalCard = repository.findById(cardId);
        if (!optionalCard.isPresent()) return status(NOT_FOUND).body("Card not found");
        Card card = optionalCard.get();
        card.setEnabled(isActive);
        return ok(new CardResponse(repository.save(card)));
    }

    public ResponseEntity<?> deleteCard(UUID id) {
        Optional<Card> optionalCard = repository.findById(id);
        if (!optionalCard.isPresent()) return notFound().build();
        try {
            repository.delete(optionalCard.get());
            return noContent().build();
        } catch (Exception exception) {
            return badRequest().build();
        }
    }
}
