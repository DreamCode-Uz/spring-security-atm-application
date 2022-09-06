package uz.pdp.springsecurityatm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurityatm.component.JWTProvider;
import uz.pdp.springsecurityatm.payload.LoginDTO;
import uz.pdp.springsecurityatm.repository.CardRepository;
import uz.pdp.springsecurityatm.repository.UserRepository;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@Service
public class AuthService implements UserDetailsService {
    private final CardRepository repository;
    private final JWTProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(CardRepository repository, JWTProvider jwtProvider, @Lazy AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByCardNumber(username).orElse(null);
    }

    public ResponseEntity<?> login(LoginDTO dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getCardNumber(), dto.getPinCode()));
            return ok(jwtProvider.generateToken(authentication));
        } catch (BadCredentialsException e) {
            return status(401).body("The card number or pin code was entered incorrectly. Please check and try again");
        }
    }
}
