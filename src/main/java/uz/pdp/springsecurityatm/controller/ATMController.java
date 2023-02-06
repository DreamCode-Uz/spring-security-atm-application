package uz.pdp.springsecurityatm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurityatm.payload.AtmDTO;
import uz.pdp.springsecurityatm.payload.TakeOutDto;
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<?> getAll() {
        return atmService.getAllATMs();
    }

    @Secured({"ROLE_MANAGER", "ROLE_DIRECTOR"})
    @GetMapping("/{atmId}")
    public ResponseEntity<?> getOne(@PathVariable(name = "atmId") Long id) {
        return atmService.getOneAtm(id);
    }

    @Secured("ROLE_MANAGER")
    @GetMapping("/summa")
    public ResponseEntity<?> getSums(@RequestParam(name = "type", defaultValue = "uzs") String type) {
        return atmService.getSumma(type);
    }

    @Secured({"ROLE_MANAGER", "ROLE_DIRECTOR"})
    @PostMapping
    public ResponseEntity<?> saveAtm(@RequestBody @Valid AtmDTO atmDTO) {
        return atmService.saveATM(atmDTO);
    }

    @PostMapping("/{atmId}/withdraw")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> takeOut(@PathVariable(name = "atmId") Long atmId, @RequestBody @Valid TakeOutDto dto) {
        return atmService.takeOut(atmId, dto);
    }

    @Secured("ROLE_MANAGER")
    @PutMapping("/{atmId}")
    public ResponseEntity<?> editAtm(@PathVariable(name = "atmId") Long id, @RequestBody @Valid AtmDTO atmDTO) {
        return atmService.editAtm(id, atmDTO);
    }

    @Secured("ROLE_MANAGER")
    @DeleteMapping("/{atmId}")
    public ResponseEntity<?> deleteItem(@PathVariable(name = "atmId") Long id) {
        return atmService.deleteAtm(id);
    }
}
