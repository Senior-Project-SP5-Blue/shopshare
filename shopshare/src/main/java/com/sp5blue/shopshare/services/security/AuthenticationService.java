package com.sp5blue.shopshare.services.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp5blue.shopshare.exceptions.authentication.UserAlreadyExistsException;
import com.sp5blue.shopshare.exceptions.token.InvalidRefreshTokenException;
import com.sp5blue.shopshare.models.Shopper;
import com.sp5blue.shopshare.models.Token;
import com.sp5blue.shopshare.models.TokenType;
import com.sp5blue.shopshare.security.request.SignInRequest;
import com.sp5blue.shopshare.security.request.SignUpRequest;
import com.sp5blue.shopshare.security.response.AuthenticationResponse;
import com.sp5blue.shopshare.services.shopper.IShopperService;
import com.sp5blue.shopshare.services.token.ITokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class AuthenticationService implements IAuthenticationService {
    private final IShopperService shopperService;

    private final ITokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    public AuthenticationService(IShopperService shopperService, ITokenService tokenService, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.shopperService = shopperService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public AuthenticationResponse signUp(SignUpRequest request) throws UserAlreadyExistsException {
        if (shopperService.existsByUsername(request.username())) throw new UserAlreadyExistsException("An account with entered username already exists - " + request.username());
        if (shopperService.existsByEmail(request.email())) throw new UserAlreadyExistsException("An account with entered email already exists - " + request.email());
        Shopper user = new Shopper(
                request.firstName(),
                request.lastName(),
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password()));
        Shopper savedUser = shopperService.create(user);
        final String accessToken = jwtService.generateToken(user);
        final String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, accessToken, TokenType.ACCESS);
        saveUserToken(savedUser, refreshToken, TokenType.REFRESH);
        return new AuthenticationResponse(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public AuthenticationResponse signIn(SignInRequest request) {
        Shopper user = shopperService.readByEmail(request.email());
        System.out.printf("Shopper is %s\n", user);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        final String accessToken = jwtService.generateToken(user);
        final String refreshToken = jwtService.generateRefreshToken(user);
        tokenService.revokeAllUserTokens(user.getId());
//        _revokeAllTokens(user);
        saveUserToken(user, accessToken, TokenType.ACCESS);
        saveUserToken(user, refreshToken, TokenType.REFRESH);
        return new AuthenticationResponse(accessToken, refreshToken);
    }


    @Override
    @Transactional
    public void saveUserToken(Shopper user, String token, TokenType tokenType) {
        Token _token = new Token(token, user, tokenType);
        tokenService.create(_token);
    }

    @Override
    @Transactional
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) return;

        refreshToken = authHeader.substring(7);

        if (!jwtService.extractRefreshStatus(refreshToken)) throw new InvalidRefreshTokenException("Token is not a valid refresh token - " + refreshToken);
        String userName = jwtService.extractSubject(refreshToken);

        if (userName != null) {
            Shopper shopper = shopperService.readByUsername(userName);

            if (jwtService.validateToken(refreshToken, shopper)) {
                String accessToken = jwtService.generateToken(shopper);
                saveUserToken(shopper, accessToken, TokenType.ACCESS);
                AuthenticationResponse authenticationResponse = new AuthenticationResponse(accessToken, refreshToken);
                try {
                    new ObjectMapper().writeValue(response.getOutputStream(), authenticationResponse);
                }
                catch (IOException ioException) {
                    logger.error(ioException.getMessage());
                }
            }
        }
    }


    @Override
    @Transactional
    public void _revokeAllTokens(Shopper shopper) {
        List<Token> validTokens = tokenService.readAllByShopperId(shopper.getId(), true);
        if (validTokens.isEmpty()) return;

        validTokens.forEach(t -> {
            t.setRevoked(true);
            t.setExpired(true);
        });
    }
}
