package uz.pdp.springsecurityatm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurityatm.repository.RoleRepository;

import static org.springframework.http.ResponseEntity.ok;

@Service
public class RoleService {
    private final RoleRepository repository;

    @Autowired
    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<?> getRoles() {
        return ok(repository.findAll());
    }
}
