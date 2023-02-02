package uz.pdp.springsecurityatm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurityatm.entity.ATM;
import uz.pdp.springsecurityatm.repository.ATMRepository;

import static org.springframework.http.ResponseEntity.ok;

@Service
public class AtmService {

    private final ATMRepository repository;

    @Autowired
    public AtmService(ATMRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<?> getAllATMs() {
        return ok(repository.findAll());
    }

    public ResponseEntity<?> getOneAtm(Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> saveATM(ATM atm) {}
}
