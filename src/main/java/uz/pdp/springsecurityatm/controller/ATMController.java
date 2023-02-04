package uz.pdp.springsecurityatm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurityatm.payload.ATMDTO;
import uz.pdp.springsecurityatm.service.AtmService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/atm")
public class ATMController {
    private final AtmService atmService;

    @Autowired
    public ATMController(AtmService atmService) {
        this.atmService = atmService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return atmService.getAllATMs();
    }

    @GetMapping("/{atmId}")
    public ResponseEntity<?> getOne(@PathVariable(name = "atmId") Long id) {
        return atmService.getOneAtm(id);
    }

    @GetMapping("/summa")
    public ResponseEntity<?> getSums(@RequestParam(name = "type", defaultValue = "uzs") String type) {
        return atmService.getSumma(type);
    }

    @PostMapping
    public ResponseEntity<?> saveAtm(@RequestBody @Valid ATMDTO atmDTO) {
        return atmService.saveATM(atmDTO);
    }

    @PutMapping("/{atmId}")
    public ResponseEntity<?> editAtm(@PathVariable(name = "atmId") Long id, @RequestBody @Valid ATMDTO atmDTO) {
        return atmService.editAtm(id, atmDTO);
    }

    @DeleteMapping("/{atmId}")
    public ResponseEntity<?> deleteItem(@PathVariable(name = "atmId") Long id) {
        return atmService.deleteAtm(id);
    }
}
