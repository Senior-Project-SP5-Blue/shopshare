package com.sp5blue.shopshare.services.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp5blue.shopshare.dtos.auth.AuthenticationResponse;
import com.sp5blue.shopshare.dtos.auth.SignInRequest;
import com.sp5blue.shopshare.dtos.auth.SignUpRequest;
import com.sp5blue.shopshare.dtos.user.UserDto;
import com.sp5blue.shopshare.events.OnSignUpCompleteEvent;
import com.sp5blue.shopshare.exceptions.authentication.UserAlreadyExistsException;
import com.sp5blue.shopshare.exceptions.token.InvalidTokenException;
import com.sp5blue.shopshare.models.user.Token;
import com.sp5blue.shopshare.models.user.TokenType;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.services.token.ITokenService;
import com.sp5blue.shopshare.services.user.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
public class AuthenticationService implements IAuthenticationService {
    private final IUserService userService;

    private final ITokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final ApplicationEventPublisher eventPublisher;

    final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    public AuthenticationService(IUserService userService, ITokenService tokenService, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    @Async
    public void signUp(SignUpRequest request) throws UserAlreadyExistsException {
        CompletableFuture<Boolean> getUsernameExists = userService.userExistsByUsername(request.username());
        CompletableFuture<Boolean> getEmailExists = userService.userExistsByEmail(request.email());
        CompletableFuture.allOf(getUsernameExists, getEmailExists).join();

        User user = new User(
                request.firstName(),
                request.lastName(),
                request.username(),
                request.email(),
                request.number(),
                passwordEncoder.encode(request.password()));
        user = userService.createOrSaveUser(user).join();
        eventPublisher.publishEvent(new OnSignUpCompleteEvent(this, user));
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<AuthenticationResponse> signIn(SignInRequest request) {
        CompletableFuture<User> getUser = userService.getUserByEmail(request.email());
        User user = getUser.join();
        User loggerInUser = (User) authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password())).getPrincipal();
        final String accessJwt = jwtService.generateAccessToken(user);
        final String refreshJwt = jwtService.generateRefreshToken(user);
        Token accessToken = new Token(accessJwt, user, TokenType.ACCESS);
        Token refreshToken = new Token(refreshJwt, user, TokenType.REFRESH);
        tokenService.revokeAllUserTokens(user.getId());
        saveUserTokens(accessToken, refreshToken);
        return CompletableFuture.completedFuture(new AuthenticationResponse(accessJwt, refreshJwt, new UserDto(loggerInUser)));
    }

    @Override
    @Transactional
    @Async
    public void confirmEmail(String token) throws InvalidTokenException {
        Token confirmToken = tokenService.readByConfirmationToken(token).join();
        if (confirmToken.isExpired()) throw new InvalidTokenException("Confirmation token expired");
        User user = confirmToken.getUser();
        user.setActive(true);
        confirmToken.setExpired(true);
        userService.createOrSaveUser(user).join();
        tokenService.createOrSave(confirmToken).join();
    }


    @Override
    @Transactional
    @Async
    public void saveUserToken(User user, String token, TokenType tokenType) {
        Token _token = new Token(token, user, tokenType);
        tokenService.createOrSave(_token).join();
    }

    @Override
    @Transactional
    @Async
    public void saveUserTokens(Token... tokens) {
        tokenService.createOrSave(tokens);
    }

    @Override
    @Transactional
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) return;

        refreshToken = authHeader.substring(7);

        if (!jwtService.extractRefreshStatus(refreshToken)) throw new InvalidTokenException("Token is not a valid refresh token - " + refreshToken);
        String userName = jwtService.extractSubject(refreshToken);

        if (userName != null) {
            User user = userService.getUserByUsername(userName).join();

            if (jwtService.validateToken(refreshToken, user)) {
                String accessToken = jwtService.generateAccessToken(user);
                saveUserTokens(new Token(accessToken, user, TokenType.ACCESS));
                AuthenticationResponse authenticationResponse = new AuthenticationResponse(accessToken, refreshToken, new UserDto((user)));
                response.setHeader("Content-Type", "application/json");
                try {
                    new ObjectMapper().writeValue(response.getOutputStream(), authenticationResponse);
                }
                catch (IOException ioException) {
                    logger.error(ioException.getMessage());
                }
            }
        }
    }
}
