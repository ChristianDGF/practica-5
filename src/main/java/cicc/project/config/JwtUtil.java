package cicc.project.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtUtil {
    
    private final String SECRET_KEY = "pucmm_icc354_web_avanzada_secreto_clave";

    public String generateToken(String username, LocalDateTime expiration) {
        Date expDate = Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant());
        
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expDate)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}