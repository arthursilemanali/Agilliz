package agiliz.projetoAgiliz.configs.security.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
public class GerenciadorTokenJWT {
    private final SecretKey secretKey = Keys.hmacShaKeyFor(
            "cPqikLBo7pdyw0LOuYgQYq5mRYfWlv7tsmWguN39wONXZ7GvmUZKZUwJLsRTA7GrS5JYTHvWEsFIlxNdUxZAC7oE3l8N1OIZn26SQqGx2Vi26zPmBeFgblF4WzH8GpnzmI4kGf7AD4rIl14I0mQbT3VdSakxKpSRiFV6UWPSwNjKMTazcPjhzKBhqGQqcxnyrR22Y3ePI6C3eMb4MVvznQ27eXXYrHf23cncHnPSGA"
                    .getBytes(StandardCharsets.UTF_8)
    );
    private long jwtTokenValidity = 36000;

    public String getUsernameFromToken(String token) {
        try {
            return getClaimForToken(token, Claims::getSubject);
        } catch (Exception e) {
            return null;
        }
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimForToken(token, Claims::getExpiration);
    }

    public String generateToken(String email, List<String> roles) {
        return Jwts.builder()
                .setSubject(email)
                .claim("authorities", roles)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .setIssuedAt(new Date())
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token).getBody();
    }

    public <T> T getClaimForToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername());
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date(System.currentTimeMillis()));
    }
}