package com.sp5blue.shopshare.services.security;

import com.sp5blue.shopshare.exceptions.authentication.UserAlreadyExistsException;
import com.sp5blue.shopshare.exceptions.authentication.UserNotFoundException;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.security.request.SignInRequest;
import com.sp5blue.shopshare.security.request.SignUpRequest;
import com.sp5blue.shopshare.services.user.UserService;
import com.sp5blue.shopshare.services.token.TokenService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private UserService userService;

    @Mock
    TokenService tokenService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    static SignInRequest signInRequest;
    static SignUpRequest signUpRequest;

    @BeforeAll
    static void beforeAll() {
        signUpRequest = new SignUpRequest("usertest", "user", "last", "userlast@email.com", "yes");
        signInRequest = new SignInRequest("hey@email.com", "password");
    }

    @Test
    void signUp_DuplicateUsername_ThrowsUserAlreadyExistsException() {
        when(userService.userExistsByUsername(anyString())).thenReturn(CompletableFuture.completedFuture(true));
        when(userService.userExistsByEmail(anyString())).thenReturn(CompletableFuture.completedFuture(false));

        var exception = assertThrows(UserAlreadyExistsException.class, () -> authenticationService.signUp(signUpRequest));
        assertEquals("An account with entered username already exists - usertest", exception.getMessage());
    }

    @Test
    void signUp_DuplicateEmail_ThrowsUserAlreadyExistsException() {
        when(userService.userExistsByUsername(anyString())).thenReturn(CompletableFuture.completedFuture(false));
        when(userService.userExistsByEmail(anyString())).thenReturn(CompletableFuture.completedFuture(true));

        var exception = assertThrows(UserAlreadyExistsException.class, () -> authenticationService.signUp(signUpRequest));
        assertEquals("An account with entered email already exists - userlast@email.com", exception.getMessage());
    }

    @Test
    void signUp_Valid_CreatesNewShopper() throws ExecutionException, InterruptedException {
        final ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        User user = new User("usertest", "user", "last", "userlast@email.com", "yes");
        when(userService.userExistsByUsername(anyString())).thenReturn(CompletableFuture.completedFuture(false));
        when(userService.userExistsByEmail(anyString())).thenReturn(CompletableFuture.completedFuture(false));
        when(userService.createUser(any(User.class))).thenReturn(CompletableFuture.completedFuture(user));

        var _result = authenticationService.signUp(signUpRequest);
        var result = _result.get();

        verify(userService).createUser(captor.capture());
        verify(jwtService).generateToken(captor.getValue());
        assertNotNull(result);
    }

    @Test
    void signIn_UserDoesNotExist_ThrowsUserDoesNotExistException() {
        when(userService.getUserByEmail(anyString())).thenThrow( new UserNotFoundException("User does not exist - " + signInRequest.email()));
        var exception = assertThrows(UserNotFoundException.class, () -> authenticationService.signIn(signInRequest));
        assertEquals("User does not exist - hey@email.com", exception.getMessage());
    }

    @Test
    void signIn_Valid_ReturnsJwt() {
        when(userService.getUserByEmail(anyString())).thenReturn(CompletableFuture.completedFuture(new User("hey", "last", "heyLast", "hey@email.com", "password")));

        var result = authenticationService.signIn(signInRequest);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(any());
        assertNotNull(result);
    }
}