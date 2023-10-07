package com.sp5blue.shopshare.services.security;

import com.sp5blue.shopshare.exceptions.authentication.UserAlreadyExistsException;
import com.sp5blue.shopshare.exceptions.authentication.UserNotFoundException;
import com.sp5blue.shopshare.models.Shopper;
import com.sp5blue.shopshare.repositories.ShopperRepository;
import com.sp5blue.shopshare.security.request.SignInRequest;
import com.sp5blue.shopshare.security.request.SignUpRequest;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private  ShopperRepository shopperRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

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
        when(shopperRepository.existsByUsername(anyString())).thenReturn(true);

        var exception = assertThrows(UserAlreadyExistsException.class, () -> authenticationService.signUp(signUpRequest));
        assertEquals("An account with entered username already exists - usertest", exception.getMessage());
    }

    @Test
    void signUp_DuplicateEmail_ThrowsUserAlreadyExistsException() {
        when(shopperRepository.existsByUsername(anyString())).thenReturn(false);
        when(shopperRepository.existsByEmail(anyString())).thenReturn(true);

        var exception = assertThrows(UserAlreadyExistsException.class, () -> authenticationService.signUp(signUpRequest));
        assertEquals("An account with entered email already exists - userlast@email.com", exception.getMessage());
    }

    @Test
    void signUp_Valid_CreatesNewShopper() {
        final ArgumentCaptor<Shopper> captor = ArgumentCaptor.forClass(Shopper.class);

        var result = authenticationService.signUp(signUpRequest);

        verify(shopperRepository).save(captor.capture());
        verify(jwtService).generateToken(captor.getValue());
        assertNotNull(result);
    }

    @Test
    void signIn_UserDoesNotExist_ThrowsUserDoesNotExistException() {
        var exception = assertThrows(UserNotFoundException.class, () -> authenticationService.signIn(signInRequest));
        assertEquals("User with email does not exist - hey@email.com", exception.getMessage());
    }

    @Test
    void signIn_Valid_ReturnsJwt() {
        when(shopperRepository.findByEmail(anyString())).thenReturn(Optional.of(new Shopper("hey", "last", "heyLast", "hey@email.com", "password")));

        var result = authenticationService.signIn(signInRequest);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(any());
        assertNotNull(result);
    }
}