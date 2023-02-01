package uz.pdp.springsecurityatm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurityatm.entity.Bank;
import uz.pdp.springsecurityatm.repository.BankRepository;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.*;

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

    public ResponseEntity<?> getOneBank(Integer id) {
        return bankRepository.findById(id).map(ResponseEntity::ok).orElseGet(() -> notFound().build());
    }

    public ResponseEntity<?> addBank(Bank bank) {
        if (bankRepository.existsByName(bank.getName())) return status(CONFLICT).body("Name already exist");
        return status(CREATED).body(bankRepository.save(new Bank(null, bank.getName())));
    }

    public ResponseEntity<?> editBank(Integer id, Bank bank) {
        return bankRepository.findById(id)
                .map(b -> {
                    if (bankRepository.existsByName(bank.getName())) return status(CONFLICT).body("Name already exist");
                    b.setName(bank.getName());
                    return status(CREATED).body(bankRepository.save(b));
                })
                .orElseGet(() -> notFound().build());
    }

    public ResponseEntity<?> deleteBank(Integer id) {
        return bankRepository.findById(id)
                .map(bank -> {
                    try {
                        bankRepository.delete(bank);
                        return status(HttpStatus.NO_CONTENT).body("Bank deleted");
                    } catch (Exception e) {
                        return status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
                    }
                })
                .orElseGet(() -> notFound().build());
    }
}
