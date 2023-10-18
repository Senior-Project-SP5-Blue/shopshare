package com.sp5blue.shopshare.services.security;

import com.sp5blue.shopshare.exceptions.authentication.UserAlreadyExistsException;
import com.sp5blue.shopshare.exceptions.authentication.UserNotFoundException;
import com.sp5blue.shopshare.models.shopper.Shopper;
import com.sp5blue.shopshare.security.request.SignInRequest;
import com.sp5blue.shopshare.security.request.SignUpRequest;
import com.sp5blue.shopshare.services.shopper.ShopperService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private ShopperService shopperService;

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
        when(shopperService.shopperExistsByUsername(anyString())).thenReturn(true);

        var exception = assertThrows(UserAlreadyExistsException.class, () -> authenticationService.signUp(signUpRequest));
        assertEquals("An account with entered username already exists - usertest", exception.getMessage());
    }

    @Test
    void signUp_DuplicateEmail_ThrowsUserAlreadyExistsException() {
        when(shopperService.shopperExistsByUsername(anyString())).thenReturn(false);
        when(shopperService.shopperExistsByEmail(anyString())).thenReturn(true);

        var exception = assertThrows(UserAlreadyExistsException.class, () -> authenticationService.signUp(signUpRequest));
        assertEquals("An account with entered email already exists - userlast@email.com", exception.getMessage());
    }

    @Test
    void signUp_Valid_CreatesNewShopper() {
        final ArgumentCaptor<Shopper> captor = ArgumentCaptor.forClass(Shopper.class);

        var result = authenticationService.signUp(signUpRequest);

        verify(shopperService).createShopper(captor.capture());
        verify(jwtService).generateToken(captor.getValue());
        assertNotNull(result);
    }

    @Test
    void signIn_UserDoesNotExist_ThrowsUserDoesNotExistException() {
        when(shopperService.readShopperByEmail(anyString())).thenThrow( new UserNotFoundException("User does not exist - " + signInRequest.email()));
        var exception = assertThrows(UserNotFoundException.class, () -> authenticationService.signIn(signInRequest));
        assertEquals("User does not exist - hey@email.com", exception.getMessage());
    }

    @Test
    void signIn_Valid_ReturnsJwt() {
        when(shopperService.readShopperByEmail(anyString())).thenReturn(new Shopper("hey", "last", "heyLast", "hey@email.com", "password"));

        var result = authenticationService.signIn(signInRequest);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(any());
        assertNotNull(result);
    }
}