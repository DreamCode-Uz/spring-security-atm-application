package uz.pdp.springsecurityatm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurityatm.payload.UserDTO;
import uz.pdp.springsecurityatm.service.UserService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getUsers() {
        return service.getUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable(name = "userId") UUID userId) {
        return service.getUser(userId);
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody@Valid UserDTO userDTO) {
        return service.addUser(userDTO);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> editUser(@PathVariable(name = "userId") UUID userId, @RequestBody @Valid UserDTO dto) {
        return service.editUser(userId, dto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "userId") UUID userId) {
        return service.deleteUser(userId);
    }
}
