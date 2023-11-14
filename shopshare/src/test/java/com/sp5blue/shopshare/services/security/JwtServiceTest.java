package com.sp5blue.shopshare.services.security;

import com.sp5blue.shopshare.models.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JwtService.class)
//@TestPropertySource(properties = {"${jwt.secret-key} = 5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437"} )
class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    private static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";


    private Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }



    @Test
    void generateToken() {
        User user = new User("Jack", "Ripper", "jackr", "jackr@email.com", "jackpassword");
        ReflectionTestUtils.setField(jwtService, "SECRET", SECRET);
        String result  = jwtService.generateAccessToken(user);

        assertNotNull(result);
    }

    @Test
    void extractSubject() {
        String subject = "subject";
        ReflectionTestUtils.setField(jwtService, "SECRET", SECRET);
        String token = Jwts.builder().setSubject(subject)
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();

        String result  = jwtService.extractSubject(token);

        assertEquals(subject, result);
    }

    @Test
    void extractExpiration() {
        ReflectionTestUtils.setField(jwtService, "SECRET", SECRET);
        Date expirationDate = new Date(2023, Calendar.SEPTEMBER, 23);
        String token = Jwts.builder()
                .setExpiration(expirationDate)
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();

        Date result  = jwtService.extractExpiration(token);
        assertEquals(expirationDate, result);
    }

    @Test
    void extractClaim() {
        ReflectionTestUtils.setField(jwtService, "SECRET", SECRET);
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
        ReflectionTestUtils.setField(jwtService, "SECRET", SECRET);
        User user = new User("User", "Test", "username1", "user@email.com", "password");
        String token = Jwts.builder().setSubject("username1")
                .setExpiration(new Date(System.currentTimeMillis() + 860000))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();

        boolean result = jwtService.validateToken(token, user);
        assertTrue(result);

    }
}