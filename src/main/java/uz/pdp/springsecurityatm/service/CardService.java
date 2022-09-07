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
import uz.pdp.springsecurityatm.payload.action.Action;
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

    @Autowired
    public CardService(CardRepository repository, UserRepository userRepository, RoleRepository roleRepository, BankRepository bankRepository, CardTypeRepository typeRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bankRepository = bankRepository;
        this.typeRepository = typeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> getAllCards(Integer page, Integer size) {
        Page<CardResponse> all = repository.findAll(PageRequest.of(page > 0 ? page - 1 : 0, size > 0 ? size : 10)).map(CardResponse::new);
        return ok(all);
    }

    public ResponseEntity<?> getCard(UUID id) {
        Optional<Card> optionalCard = repository.findById(id);
        return status(optionalCard.isPresent() ? OK : NOT_FOUND).body(optionalCard.isPresent() ? new CardResponse(optionalCard.get()) : "Card not found");
    }

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

    public ResponseEntity<?> getAllCardTypes() {
        return ok(typeRepository.findAll());
    }
}
