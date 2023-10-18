package com.sp5blue.shopshare.services.token;

import com.sp5blue.shopshare.exceptions.token.TokenNotFoundException;
import com.sp5blue.shopshare.models.shopper.Shopper;
import com.sp5blue.shopshare.models.shopper.Token;
import com.sp5blue.shopshare.models.shopper.TokenType;
import com.sp5blue.shopshare.repositories.TokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    TokenRepository mockTokenRepository;

    @InjectMocks
    TokenService tokenService;

    @Test
    void create_CreatesNewToken_ReturnsNewToken() {
        Shopper shopper = new Shopper();
        Token token = new Token("jwt", shopper);
        when(mockTokenRepository.save(token)).thenReturn(token);

        var result = tokenService.create(token);
        assertEquals(token, result);
    }

    @Test
    void readAllByShopperId_ValidOnly_ReturnsAllValidTokens() {
        Shopper shopper1 = new Shopper();
        Token token1 = new Token("jwt", shopper1);
        Token token2 = new Token("jwt", shopper1, TokenType.REFRESH);
        Token token3 = new Token("jwt", shopper1);
        token3.setRevoked(true);
        token3.setExpired(true);
        Token token4 = new Token("jwt", shopper1);
        when(mockTokenRepository.findAllValidTokensByShopper_Id(shopper1.getId())).thenReturn(List.of(token1, token2, token4));

        var results = tokenService.readAllByShopperId(shopper1.getId(), true);

        assertEquals(3, results.size());
        assertAll(
                () -> assertEquals(token1, results.get(0)),
                () -> assertEquals(token2, results.get(1)),
                () -> assertEquals(token4, results.get(2))
        );
    }

    @Test
    void readAllByShopperId_All_ReturnsAllTokens() {
        Shopper shopper1 = new Shopper();
        Token token1 = new Token("jwt", shopper1);
        Token token2 = new Token("jwt", shopper1, TokenType.REFRESH);
        Token token3 = new Token("jwt", shopper1);
        token3.setRevoked(true);
        token3.setExpired(true);
        Token token4 = new Token("jwt", shopper1);
        when(mockTokenRepository.findAllByShopper_Id(shopper1.getId())).thenReturn(List.of(token1, token2, token3, token4));

        var results = tokenService.readAllByShopperId(shopper1.getId(), false);

        assertEquals(4, results.size());
        assertAll(
                () -> assertEquals(token1, results.get(0)),
                () -> assertEquals(token2, results.get(1)),
                () -> assertEquals(token3, results.get(2)),
                () -> assertEquals(token4, results.get(3))
        );
    }

    @Test
    void readAllAccessByShopperId_ValidOnly_ReturnsAllValidAccessTokens() {
        Shopper shopper1 = new Shopper();
        Token token1 = new Token("jwt", shopper1);
        Token token2 = new Token("jwt", shopper1, TokenType.REFRESH);
        Token token3 = new Token("jwt", shopper1);
        token3.setRevoked(true);
        token3.setExpired(true);
        Token token4 = new Token("jwt", shopper1);
        when(mockTokenRepository.findAllValidAccessTokensByShopper_Id(shopper1.getId())).thenReturn(List.of(token1, token4));

        var results = tokenService.readAllAccessByShopperId(shopper1.getId(), true);

        assertEquals(2, results.size());
        assertAll(
                () -> assertEquals(token1, results.get(0)),
                () -> assertEquals(token4, results.get(1))
        );
    }

    @Test
    void readAllAccessByShopperId_All_ReturnsAllAccessTokens() {
        Shopper shopper1 = new Shopper();
        Token token1 = new Token("jwt", shopper1);
        Token token2 = new Token("jwt", shopper1, TokenType.REFRESH);
        Token token3 = new Token("jwt", shopper1);
        token3.setRevoked(true);
        token3.setExpired(true);
        Token token4 = new Token("jwt", shopper1);
        when(mockTokenRepository.findAllAccessTokensByShopper_Id(shopper1.getId())).thenReturn(List.of(token1, token3, token4));

        var results = tokenService.readAllAccessByShopperId(shopper1.getId(), false);

        assertEquals(3, results.size());
        assertAll(
                () -> assertEquals(token1, results.get(0)),
                () -> assertEquals(token3, results.get(1)),
                () -> assertEquals(token4, results.get(2))
        );
    }

    @Test
    void readRefreshByShopperId_None_ThrowsTokenNotFoundException() {
        Shopper shopper1 = new Shopper();
        Token token1 = new Token("jwt1", shopper1);
        Token token2 = new Token("jwt2", shopper1, TokenType.REFRESH);
        Token token3 = new Token("jwt3", shopper1);
        token3.setRevoked(true);
        token3.setExpired(true);
        Token token4 = new Token("jwt4", shopper1);
        when(mockTokenRepository.findRefreshTokenByShopper_Id(shopper1.getId())).thenReturn(Optional.empty());

        var exception = assertThrows(TokenNotFoundException.class, () -> tokenService.readRefreshByShopperId(shopper1.getId()));
        assertEquals("User - " + shopper1.getId() + " does not have valid refresh token",exception.getMessage());
    }

    @Test
    void readByToken_NotValid_ThrowsTokenNotFoundException() {
        Shopper shopper1 = new Shopper();
        Token token1 = new Token("jwt1", shopper1);
        when(mockTokenRepository.findByToken(token1.getToken())).thenReturn(Optional.empty());

        var exception = assertThrows(TokenNotFoundException.class, () -> tokenService.readByToken(token1.getToken()));
        assertEquals("Token does not exist - " + token1.getToken(), exception.getMessage());
    }

    @Test
    void readByToken_Valid_ReturnsToken() {
        Shopper shopper1 = new Shopper();
        Token token1 = new Token("jwt1", shopper1);
        when(mockTokenRepository.findByToken(token1.getToken())).thenReturn(Optional.of(token1));

        var result = tokenService.readByToken(token1.getToken());
        assertEquals(token1, result);
    }

    @Test
    void revokeAccessToken_NotValid_ThrowsTokenNotFoundException() {
        Shopper shopper1 = new Shopper();
        Token token1 = new Token("jwt1", shopper1);
        when(mockTokenRepository.findByToken(token1.getToken())).thenReturn(Optional.empty());

        var exception = assertThrows(TokenNotFoundException.class, () -> tokenService.revokeAccessToken(token1.getToken()));
        assertEquals("Token does not exist - " + token1.getToken(), exception.getMessage());
    }

    @Test
    void revokeAccessToken_Valid_InvalidatesAccessToken() {
        Shopper shopper1 = new Shopper();
        Token token1 = new Token("jwt1", shopper1);
        when(mockTokenRepository.findByToken(token1.getToken())).thenReturn(Optional.of(token1));

        tokenService.revokeAccessToken(token1.getToken());

        assertTrue(token1.isExpired());
        assertTrue(token1.isRevoked());
    }

    @Test
    void revokeAllTokens() {
        Shopper shopper1 = new Shopper();
        Token token1 = new Token("jwt1", shopper1);
        Token token2 = new Token("jwt2", shopper1, TokenType.REFRESH);
        Token token3 = new Token("jwt3", shopper1, TokenType.REFRESH);
        Token token4 = new Token("jwt4", shopper1);
        when(mockTokenRepository.findAllByShopper_Id(shopper1.getId())).thenReturn(Arrays.asList(token1, token2, token3, token4));

        tokenService.revokeAllUserTokens(shopper1.getId());

        assertAll(
                () -> assertTrue(token1.isExpired() && token1.isRevoked()),
                () -> assertTrue(token2.isExpired() && token2.isRevoked()),
                () -> assertTrue(token3.isExpired() && token3.isRevoked()),
                () -> assertTrue(token4.isExpired() && token4.isRevoked())
        );
    }
}