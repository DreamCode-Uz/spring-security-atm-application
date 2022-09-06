package uz.pdp.springsecurityatm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurityatm.repository.BankRepository;
import uz.pdp.springsecurityatm.repository.CardRepository;
import uz.pdp.springsecurityatm.repository.RoleRepository;
import uz.pdp.springsecurityatm.repository.UserRepository;

import static org.springframework.http.ResponseEntity.*;

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

    public ResponseEntity<?> getAllCards() {
        return ok(repository.findAll());
    }
}
