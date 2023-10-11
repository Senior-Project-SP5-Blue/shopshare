package com.sp5blue.shopshare.services.security;

import com.sp5blue.shopshare.services.token.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignOutServiceTest {

    @Mock
    TokenService mockTokenService;

    @Mock
    JwtService mockJwtService;

    @InjectMocks
    SignOutService signOutService;


    @Test
    void logout_Valid_RevokesAllUserTokens() {
        HttpServletRequest request = mock();
        HttpServletResponse response = mock();
        Authentication authentication = mock();
        UUID randomUUID = UUID.randomUUID();
        when(request.getHeader("Authorization")).thenReturn("Bearer randomToken");
        when(mockJwtService.extractId("randomToken")).thenReturn(Optional.of(randomUUID));

        signOutService.logout(request, response, authentication);

        verify(mockTokenService).revokeAllUserTokens(randomUUID);
    }
}