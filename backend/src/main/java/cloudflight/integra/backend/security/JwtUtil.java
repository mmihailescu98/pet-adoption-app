package cloudflight.integra.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
public class JwtUtil {

    private final String JWT_SECRET = "secretKey777";
    private final long JWT_EXPIRATION_TIME = 24 * 60 * 60 * 1000;

    public static CustomUserDetails getAuthenticatedUser()
    {
        return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String generateToken(String username, Set<String> roles) {
        return JWT.create()
                .withSubject(username)
                .withClaim("roles", roles.stream().toList())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(JWT_SECRET));
    }

    public boolean validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(JWT_SECRET)).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException ex) {
            return false;
        }
    }

    public String extractUsername(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getSubject();
    }

    public Set<String> extractRoles(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return Set.copyOf(jwt.getClaim("roles").asList(String.class));
    }
}
