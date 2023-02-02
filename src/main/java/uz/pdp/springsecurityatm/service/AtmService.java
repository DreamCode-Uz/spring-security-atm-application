package uz.pdp.springsecurityatm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurityatm.entity.Bank;
import uz.pdp.springsecurityatm.entity.enums.USD;
import uz.pdp.springsecurityatm.entity.enums.UZS;
import uz.pdp.springsecurityatm.payload.ATMDTO;
import uz.pdp.springsecurityatm.repository.ATMRepository;
import uz.pdp.springsecurityatm.repository.BankRepository;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.*;

@Service
public class AtmService {

    private final ATMRepository repository;
    private final BankRepository bankRepository;

    @Autowired
    public AtmService(ATMRepository repository,
                      BankRepository bankRepository) {
        this.repository = repository;
        this.bankRepository = bankRepository;
    }

    public ResponseEntity<?> getAllATMs() {
        return ok(repository.findAll());
    }

    public ResponseEntity<?> getOneAtm(Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> getSumma(String type) {
        if (type.equalsIgnoreCase("uzs")) return ok(UZS.values());
        else if (type.equalsIgnoreCase("usd")) return ok(USD.values());
        else return badRequest().body("Wrong type");
    }

    public ResponseEntity<?> saveATM(ATMDTO dto) {
        Optional<Bank> optionalBank = bankRepository.findById(dto.getBankId());
        if (optionalBank.isPresent()) return status(NOT_FOUND).body("Bank not found");
        System.out.println(dto);
        return ok("atm successfully saved");
    }

    public ResponseEntity<?> deleteAtm(Long id) {
        return null;
    }

    public ResponseEntity<?> editAtm(Long id, ATMDTO atmDTO) {
        return null;
    }
}
