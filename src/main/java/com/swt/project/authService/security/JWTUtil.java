package com.swt.project.authService.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class JWTUtil {

    @Value("${jwt_secret}")
    private String secret;



    public String generateToken(String email) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject("User Details")
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withIssuer("SWT-Projekt")
                .withExpiresAt(new Date(System.currentTimeMillis() + 1200000))
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveSubject(String token)throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("SWT-Projekt")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("email").asString();
    }

    public boolean checkTokenExpire(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        Date expirationDate = decodedJWT.getExpiresAt();
        return expirationDate.before(new Date());
    }

    public boolean isTokenFormatValid(String token) {
        String pattern = "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]*$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(token);
        if (!m.matches() || token.length() > 255 || token.length() < 10) {
            return true;
        }
        return false;
    }

    public boolean isTokenValid(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            verifier.verify(token);
            return false;
        } catch (JWTVerificationException e) {
            return true;
        }
    }
}