package uz.pdp.springsecurityatm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurityatm.entity.Card;
import uz.pdp.springsecurityatm.payload.response.CardResponse;
import uz.pdp.springsecurityatm.repository.BankRepository;
import uz.pdp.springsecurityatm.repository.CardRepository;
import uz.pdp.springsecurityatm.repository.RoleRepository;
import uz.pdp.springsecurityatm.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@Service
public class CardService {
    private final CardRepository repository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BankRepository bankRepository;

    @Autowired
    public CardService(CardRepository repository, UserRepository userRepository, RoleRepository roleRepository, BankRepository bankRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bankRepository = bankRepository;
    }

    public ResponseEntity<?> getAllCards(Integer page, Integer size) {
        Page<CardResponse> all = repository.findAll(PageRequest.of(page > 0 ? page - 1 : 0, size > 0 ? size : 10)).map(CardResponse::new);
        return ok(all);
    }

    public ResponseEntity<?> getCard(UUID id) {
        Optional<Card> optionalCard = repository.findById(id);
        return status(optionalCard.isPresent() ? OK : NOT_FOUND).body(optionalCard.isPresent() ? new CardResponse(optionalCard.get()) : "Card not found");
    }
}
