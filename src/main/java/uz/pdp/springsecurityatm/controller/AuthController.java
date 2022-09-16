package uz.pdp.springsecurityatm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurityatm.payload.LoginDTO;
import uz.pdp.springsecurityatm.service.AuthService;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO dto) {
        return authService.login(dto);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam(name = "code") UUID id, @RequestParam(name = "mail") String email) {
        return ok("Success");
    }
}
