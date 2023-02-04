package uz.pdp.springsecurityatm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurityatm.entity.Bank;
import uz.pdp.springsecurityatm.service.BankService;

import javax.validation.Valid;

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

    @GetMapping("/{bankId}")
    public ResponseEntity<?> getOne(@PathVariable(name = "bankId") Integer bankId) {
        return service.getOneBank(bankId);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid Bank bank) {
        return service.addBank(bank);
    }

    @PutMapping("/{bankId}")
    public ResponseEntity<?> update(@PathVariable(name = "bankId") Integer bankId, @RequestBody @Valid Bank bank) {
        return service.editBank(bankId, bank);
    }

    @DeleteMapping("/{bankId}")
    public ResponseEntity<?> delete(@PathVariable(name = "bankId") Integer bankId) {
        return service.deleteBank(bankId);
    }
}
