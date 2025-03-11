package com.recap.self.springcourse.security.secure;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {

    private static final String JWT_SUBJECT = "User details";
    private static final String JWT_CLAIM_USERNAME = "username";
    private static final String JWT_ISSUER = "sec_recap_app";

    @Value("${jwt_secret}")
    private String secret;

    // --- could be based on any data needed (not only username)
    public String generateToken(String username) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return JWT.create()
                .withSubject(JWT_SUBJECT)
                .withClaim(JWT_CLAIM_USERNAME, username) // --- claim is one of the key/value pairs within payload
                .withIssuedAt(new Date())
                .withIssuer(JWT_ISSUER)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public String decodeJWTAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject(JWT_SUBJECT)
                .withIssuer(JWT_ISSUER)
                .build(); // --- build verifier to JWT check (should meet the requirements: subject, issuer ...)

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim(JWT_CLAIM_USERNAME).asString();
    }

}
