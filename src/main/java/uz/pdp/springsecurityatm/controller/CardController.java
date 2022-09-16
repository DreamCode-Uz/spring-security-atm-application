package uz.pdp.springsecurityatm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurityatm.payload.CardDTO;
import uz.pdp.springsecurityatm.payload.CardManagerDTO;
import uz.pdp.springsecurityatm.service.CardService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/card")
public class CardController {

    private final CardService service;

    @Autowired
    public CardController(CardService service) {
        this.service = service;
    }

    @GetMapping("/type")
    @PreAuthorize(value = "isAuthenticated()")
    public ResponseEntity<?> getAllCardTypes() {
        return service.getAllCardTypes();
    }

    @GetMapping
    @Secured("ROLE_DIRECTOR")
    public ResponseEntity<?> getCards(@RequestParam(name = "page", defaultValue = "1") Integer page, @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return service.getAllCards(page, size);
    }

    @GetMapping("/{cardId}")
    @Secured({"ROLE_DIRECTOR", "ROLE_MANAGER"})
    public ResponseEntity<?> getCard(@PathVariable("cardId") UUID id) {
        return service.getCard(id);
    }

    @PostMapping
    @Secured({"ROLE_DIRECTOR", "ROLE_MANAGER"})
    public ResponseEntity<?> registerCard(@RequestBody @Valid CardDTO cardDTO) {
        return service.registerCard(cardDTO);
    }

    @PutMapping("/time/{cardId}")
    @Secured("ROLE_MANAGER") // BU YO'LGA FAQAT MASUL XODIM KIRA OLADI(DIRECTOR HAM KIRA OLMAYDI)
    public ResponseEntity<?> renewalOfValidityCard(@PathVariable("cardId") UUID id, @RequestParam(name = "year", defaultValue = "1") Integer year) {
        return service.renewalOfValidity(id, year);
    }

    @PutMapping("/status/{cardId}")
    @Secured("ROLE_MANAGER")
    public ResponseEntity<?> activateOrDeactivateCard(@PathVariable("cardId") UUID id, @RequestParam(name = "active", defaultValue = "false") boolean active) {
        return service.activateOrDeactivateTheCard(id, active);
    }

    @PostMapping("/manager")
    @Secured("ROLE_DIRECTOR")
    public ResponseEntity<?> registerManager(@RequestBody @Valid CardManagerDTO cardManagerDTO) {
        return service.registerManager(cardManagerDTO);
    }

    @DeleteMapping("/{cardId}")
    @Secured({"ROLE_DIRECTOR", "ROLE_MANAGER"})
    public ResponseEntity<?> deleteCard(@PathVariable("cardId") UUID id) {
        return service.deleteCard(id);
    }
}
