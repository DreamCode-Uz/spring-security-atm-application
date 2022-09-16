package uz.pdp.springsecurityatm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.springsecurityatm.entity.Bank;
import uz.pdp.springsecurityatm.service.BankService;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    private final BankService service;

    @Autowired
    public BankController(BankService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return service.getAllBank();
    }

    @PostMapping
    public ResponseEntity<?> save(Bank bank) {
        return service.addBank(bank);
    }
}
