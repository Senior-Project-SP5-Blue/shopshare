package com.sp5blue.shopshare.services.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp5blue.shopshare.exceptions.authentication.UserAlreadyExistsException;
import com.sp5blue.shopshare.exceptions.token.InvalidRefreshTokenException;
import com.sp5blue.shopshare.models.user.Token;
import com.sp5blue.shopshare.models.user.TokenType;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.security.request.SignInRequest;
import com.sp5blue.shopshare.security.request.SignUpRequest;
import com.sp5blue.shopshare.security.response.AuthenticationResponse;
import com.sp5blue.shopshare.services.token.ITokenService;
import com.sp5blue.shopshare.services.user.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AuthenticationService implements IAuthenticationService {
    private final IUserService userService;

    private final ITokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    public AuthenticationService(IUserService userService, ITokenService tokenService, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<AuthenticationResponse> signUp(SignUpRequest request) throws UserAlreadyExistsException {
        CompletableFuture<Boolean> getUsernameExists = userService.userExistsByUsername(request.username());
        CompletableFuture<Boolean> getEmailExists = userService.userExistsByEmail(request.email());
        CompletableFuture.allOf(getUsernameExists, getEmailExists).join();

        if (getUsernameExists.join()) throw new UserAlreadyExistsException("An account with entered username already exists - " + request.username());
        if (getEmailExists.join()) throw new UserAlreadyExistsException("An account with entered email already exists - " + request.email());
        User user = new User(
                request.firstName(),
                request.lastName(),
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password()));
        User savedUser = userService.createUser(user).join();
        final String accessToken = jwtService.generateToken(user);
        final String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, accessToken, TokenType.ACCESS);
        saveUserToken(savedUser, refreshToken, TokenType.REFRESH);
        return CompletableFuture.completedFuture(new AuthenticationResponse(accessToken, refreshToken));
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<AuthenticationResponse> signIn(SignInRequest request) {
        CompletableFuture<User> getUser = userService.getUserByEmail(request.email());
        User user = getUser.join();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        final String accessToken = jwtService.generateToken(user);
        final String refreshToken = jwtService.generateRefreshToken(user);
        tokenService.revokeAllUserTokens(user.getId());
        saveUserToken(user, accessToken, TokenType.ACCESS);
        saveUserToken(user, refreshToken, TokenType.REFRESH);
        return CompletableFuture.completedFuture(new AuthenticationResponse(accessToken, refreshToken));
    }


    @Override
    @Transactional
    @Async
    public void saveUserToken(User user, String token, TokenType tokenType) {
        Token _token = new Token(token, user, tokenType);
        tokenService.create(_token);
    }

    @Override
    @Transactional
    @Async
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) return;

        refreshToken = authHeader.substring(7);

        if (!jwtService.extractRefreshStatus(refreshToken)) throw new InvalidRefreshTokenException("Token is not a valid refresh token - " + refreshToken);
        String userName = jwtService.extractSubject(refreshToken);

        if (userName != null) {
            User user = userService.getUserByUsername(userName).join();

            if (jwtService.validateToken(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user);
                saveUserToken(user, accessToken, TokenType.ACCESS);
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
    @Async
    public void _revokeAllTokens(User user) {
        List<Token> validTokens = tokenService.readAllByUserId(user.getId(), true).join();
        if (validTokens.isEmpty()) return;

        validTokens.forEach(t -> {
            t.setRevoked(true);
            t.setExpired(true);
        });
    }
}
