package uz.pdp.springsecurityatm.component;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.pdp.springsecurityatm.payload.action.Action;
import uz.pdp.springsecurityatm.service.AuthService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTProvider jwtProvider;
    private final AuthService authService;

    public JWTFilter(JWTProvider jwtProvider, AuthService authService) {
        this.jwtProvider = jwtProvider;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = Action.getToken(request);
            if (token != null && jwtProvider.validateToken(token)) {
                UserDetails userDetails = authService.loadUserByUsername(jwtProvider.getClaimsObjectFromToken(token).getSubject());
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        filterChain.doFilter(request, response);
    }
}
