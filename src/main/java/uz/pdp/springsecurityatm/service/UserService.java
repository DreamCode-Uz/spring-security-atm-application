package uz.pdp.springsecurityatm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurityatm.entity.Role;
import uz.pdp.springsecurityatm.entity.User;
import uz.pdp.springsecurityatm.entity.enums.RoleName;
import uz.pdp.springsecurityatm.payload.UserDTO;
import uz.pdp.springsecurityatm.payload.response.CardResponse;
import uz.pdp.springsecurityatm.payload.response.UserResponse;
import uz.pdp.springsecurityatm.repository.RoleRepository;
import uz.pdp.springsecurityatm.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@Service
public class UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository repository, RoleRepository roleRepository) {
        this.repository = repository;
        this.roleRepository = roleRepository;
    }

    public ResponseEntity<?> getUsers() {
        Stream<UserResponse> userResponseStream = repository.findAll()
                .stream().map(user -> new UserResponse(user, user.getCards().stream().map(CardResponse::new).collect(Collectors.toSet())));
        return ok(userResponseStream);
    }

    public ResponseEntity<?> getUser(UUID userId) {
        return repository.findById(userId)
                .map(user -> ok(new UserResponse(user, user.getCards().stream().map(CardResponse::new).collect(Collectors.toSet()))))
                .orElseGet(() -> status(NOT_FOUND).build());
    }

    public ResponseEntity<?> addUser(UserDTO dto) {
        Set<Role> roles = checkRoles(dto.getRolesId());
        if (roles.isEmpty()) {
            roleRepository.findRoleByRole(RoleName.ROLE_USER).ifPresent(roles::add);
        }
        repository.save(new User(dto.getFirstname(), dto.getLastname(), roles));
        return status(CREATED).body("User created");
    }

    public ResponseEntity<?> editUser(UUID userId, UserDTO dto) {
        return repository.findById(userId)
                .map(user -> {
                    user.setFirstName(dto.getFirstname());
                    user.setLastname(dto.getLastname());
                    Set<Role> roles = checkRoles(dto.getRolesId());
                    if (!roles.isEmpty()) user.setRoles(roles);
                    repository.save(user);
                    return status(CREATED).body("User edited");
                })
                .orElseGet(() -> status(NOT_FOUND).body("User not found"));
    }

    public ResponseEntity<?> deleteUser(UUID userId) {
        return repository.findById(userId)
                .map(user -> {
                    try {
                        repository.delete(user);
                        return status(NO_CONTENT).body("User deleted");
                    } catch (Exception e) {
                        return status(INTERNAL_SERVER_ERROR).body("Something went wrong");
                    }
                }).orElseGet(() -> status(NOT_FOUND).body("User not found"));
    }

    //    ACTIONS
    public Set<Role> checkRoles(Set<Integer> rolesId) {
        return new HashSet<>(roleRepository.findAllById(rolesId));
    }
}
