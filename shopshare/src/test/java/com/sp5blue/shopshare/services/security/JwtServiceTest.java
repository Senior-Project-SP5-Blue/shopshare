package com.sp5blue.shopshare.services.security;

import com.sp5blue.shopshare.models.Shopper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Key;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private static JwtService jwtService;

    private static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    private Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @BeforeAll
    static void beforeAll() {
        jwtService = new JwtService();
    }

    @Test
    void generateToken() {
        Shopper shopper = mock();
        when(shopper.getId()).thenReturn(UUID.randomUUID());

        String result  = jwtService.generateToken(shopper);

        assertNotNull(result);
    }

    @Test
    void extractSubject() {
        String subject = "subject";
        String token = Jwts.builder().setSubject(subject)
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();

        String result  = jwtService.extractSubject(token);

        assertEquals(subject, result);
    }

    @Test
    void extractExpiration() {
        Date expirationDate = new Date(2023, Calendar.SEPTEMBER, 23);

        String token = Jwts.builder()
                .setExpiration(expirationDate)
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();

        Date result  = jwtService.extractExpiration(token);
        assertEquals(expirationDate, result);
    }

    @Test
    void extractClaim() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", "john");
        claims.put("year", 2023);
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();

        String result1 = (String)jwtService.extractClaim(token, c -> c.get("name"));
        int result2 = (int)jwtService.extractClaim(token, c -> c.get("year"));

        assertEquals("john", result1);
        assertEquals(2023, result2);
    }

    @Test
    void validateToken() {
        Shopper shopper = new Shopper("User", "Test", "username1", "user@email.com", "password");
        String token = Jwts.builder().setSubject("username1")
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();

        boolean result = jwtService.validateToken(token, shopper);
        assertTrue(result);

    }
}