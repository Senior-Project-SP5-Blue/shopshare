package com.sp5blue.shopshare.services.security;

import com.sp5blue.shopshare.services.token.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignOutService implements LogoutHandler {
  private final TokenService tokenService;

  private final JwtService jwtService;

  @Autowired
  public SignOutService(TokenService tokenService, JwtService jwtService) {
    this.tokenService = tokenService;
    this.jwtService = jwtService;
  }

  @Override
  @Transactional
  public void logout(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    String authHeader = request.getHeader("Authorization");
    String jwt;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }
    jwt = authHeader.substring(7);
    Optional<UUID> id = jwtService.extractId(jwt);

    if (id.isEmpty()) return;
    UUID _id = id.get();

    tokenService.revokeAllUserTokens(_id);
  }
}
