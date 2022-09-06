package uz.pdp.springsecurityatm.component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;

@Component
public class JWTProvider {
    private final SecretKey key = Keys.secretKeyFor(HS512); //  SecretKey faqat 1ta toifaga bog'liq bo'lgan 3ta xususiyatni qabul qiladi(HS256, HS384, HS512)
    @Value("${spring.jwt.expiration}")
    private Long expiration;

    public String generateToken(Authentication authentication) {
        System.out.println(authentication.getPrincipal());
        return Jwts
                .builder()
                .setSubject("test")
                .setIssuedAt(new Date())    // start time
                .setExpiration(new Date(new Date().getTime() + expiration * 1000))      //  end time
                .signWith(key, HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            System.out.println("No'to'g'ri yaratilgan to'ken");
        } catch (UnsupportedJwtException e) {
            System.out.println("Token qo'llab-quvvatlanmaydi");
        } catch (ExpiredJwtException e) {
            System.out.println("Muddati o'tgan token");
        } catch (IllegalArgumentException e) {
            System.out.println("Bo'sh to'ken");
        } catch (SignatureException e) {
            System.out.println("Haqiqiy bo'lmagan token");
        }
        return false;
    }

    public Claims getClaimsObjectFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
