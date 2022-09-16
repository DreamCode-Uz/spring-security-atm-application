package uz.pdp.springsecurityatm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurityatm.entity.Bank;
import uz.pdp.springsecurityatm.repository.BankRepository;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@Service
public class BankService {
    private final BankRepository bankRepository;

    @Autowired
    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public ResponseEntity<?> getAllBank() {
        return ok(bankRepository.findAll());
    }

    public ResponseEntity<?> addBank(Bank bank) {
        if (bankRepository.existsByName(bank.getName())) return status(CONFLICT).body("Name already exist");
        return status(CREATED).body(bankRepository.save(new Bank(null, bank.getName())));
    }
}
