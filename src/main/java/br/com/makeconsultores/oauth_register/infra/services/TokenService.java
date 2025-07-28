package br.com.makeconsultores.oauth_register.infra.services;

import br.com.makeconsultores.oauth_register.infra.persistences.Access;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(Access access) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // extrai as roles do usuário
            var roles = access.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority) // ex: ROLE_ADMIN
                    .toArray(String[]::new);

            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(access.getUsername())
                    .withArrayClaim("roles", roles)
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token" + exception);
        }
    }

    public String validateTokenOld(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "INVALID TOKEN";
        }
    }

    public DecodedJWT validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer("auth-api")
                .build()
                .verify(token);
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(72).toInstant(ZoneOffset.of("-03:00"));
    }
}
