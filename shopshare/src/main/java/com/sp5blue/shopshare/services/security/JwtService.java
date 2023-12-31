package com.sp5blue.shopshare.services.security;

import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.models.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  @Value("${jwt.secret-key}")
  private String SECRET;

  @Value("${jwt.token.expiration}")
  private long jwtExpiration;

  @Value("${jwt.refresh-token.expiration}")
  private long jwtRefreshExpiration;

  Logger logger = LoggerFactory.getLogger(JwtService.class);

  public String generateAccessToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("id", ((User) userDetails).getId());
    return createToken(claims, userDetails.getUsername(), jwtExpiration);
  }

  public String generateConfirmationToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    String subject = ((User) userDetails).getEmail();
    return createToken(claims, subject, jwtExpiration);
  }

  public String generateInvitationToken(UserDetails userDetails, ShopperGroup group) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("group", group.getId());
    String subject = ((User) userDetails).getId().toString();
    return createToken(claims, subject, jwtExpiration);
  }

  public String generateRefreshToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("refresh", "true");
    return createToken(claims, userDetails.getUsername(), jwtRefreshExpiration);
  }

  private String createToken(Map<String, Object> claims, String subject, long expiration) {
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getSignKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  private Key getSignKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String extractSubject(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public Optional<UUID> extractId(String token) {
    Object _id = extractClaim(token, c -> c.get("id"));
    if (_id == null) return Optional.empty();
    return Optional.of(UUID.fromString((String) _id));
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public Boolean extractRefreshStatus(String token) {
    Object refresh = extractClaim(token, c -> c.get("refresh"));
    return refresh != null;
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractSubject(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
}
