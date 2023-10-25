package com.sp5blue.shopshare.services.token;

import com.sp5blue.shopshare.exceptions.token.TokenNotFoundException;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.models.user.Token;
import com.sp5blue.shopshare.models.user.TokenType;
import com.sp5blue.shopshare.repositories.TokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    TokenRepository mockTokenRepository;

    @InjectMocks
    TokenService tokenService;

    @Test
    void create_CreatesNewToken_ReturnsNewToken() throws ExecutionException, InterruptedException {
        User user = new User();
        Token token = new Token("jwt", user);
        when(mockTokenRepository.save(token)).thenReturn(token);

        var _result = tokenService.create(token);
        var result = _result.get();

        assertEquals(token, result);
    }

    @Test
    void readAllByShopperId_ValidOnly_ReturnsAllValidTokens() throws Exception {
        User user1 = new User();
        Token token1 = new Token("jwt", user1);
        Token token2 = new Token("jwt", user1, TokenType.REFRESH);
        Token token3 = new Token("jwt", user1);
        token3.setRevoked(true);
        token3.setExpired(true);
        Token token4 = new Token("jwt", user1);
        when(mockTokenRepository.findAllValidTokensByUser_Id(user1.getId())).thenReturn(List.of(token1, token2, token4));

        var _results = tokenService.readAllByUserId(user1.getId(), true);
        var results = _results.get();

        assertEquals(3, results.size());
        assertAll(
                () -> assertEquals(token1, results.get(0)),
                () -> assertEquals(token2, results.get(1)),
                () -> assertEquals(token4, results.get(2))
        );
    }

    @Test
    void readAllByShopperId_All_ReturnsAllTokens() throws Exception {
        User user1 = new User();
        Token token1 = new Token("jwt", user1);
        Token token2 = new Token("jwt", user1, TokenType.REFRESH);
        Token token3 = new Token("jwt", user1);
        token3.setRevoked(true);
        token3.setExpired(true);
        Token token4 = new Token("jwt", user1);
        when(mockTokenRepository.findAllByUser_Id(user1.getId())).thenReturn(List.of(token1, token2, token3, token4));

        var _results = tokenService.readAllByUserId(user1.getId(), false);
        var results = _results.get();

        assertEquals(4, results.size());
        assertAll(
                () -> assertEquals(token1, results.get(0)),
                () -> assertEquals(token2, results.get(1)),
                () -> assertEquals(token3, results.get(2)),
                () -> assertEquals(token4, results.get(3))
        );
    }

    @Test
    void readAllAccessByShopperId_ValidOnly_ReturnsAllValidAccessTokens() throws ExecutionException, InterruptedException {
        User user1 = new User();
        Token token1 = new Token("jwt", user1);
        Token token2 = new Token("jwt", user1, TokenType.REFRESH);
        Token token3 = new Token("jwt", user1);
        token3.setRevoked(true);
        token3.setExpired(true);
        Token token4 = new Token("jwt", user1);
        when(mockTokenRepository.findAllValidAccessTokensByUser_Id(user1.getId())).thenReturn(List.of(token1, token4));

        var _results = tokenService.readAllAccessByUserId(user1.getId(), true);
        var results = _results.get();

        assertEquals(2,
                results.size());
        assertAll(
                () -> assertEquals(token1, results.get(0)),
                () -> assertEquals(token4, results.get(1))
        );
    }

    @Test
    void readAllAccessByShopperId_All_ReturnsAllAccessTokens() throws ExecutionException, InterruptedException {
        User user1 = new User();
        Token token1 = new Token("jwt", user1);
        Token token2 = new Token("jwt", user1, TokenType.REFRESH);
        Token token3 = new Token("jwt", user1);
        token3.setRevoked(true);
        token3.setExpired(true);
        Token token4 = new Token("jwt", user1);
        when(mockTokenRepository.findAllAccessTokensByUser_Id(user1.getId())).thenReturn(List.of(token1, token3, token4));

        var _results = tokenService.readAllAccessByUserId(user1.getId(), false);
        var results = _results.get();

        assertEquals(3, results.size());
        assertAll(
                () -> assertEquals(token1, results.get(0)),
                () -> assertEquals(token3, results.get(1)),
                () -> assertEquals(token4, results.get(2))
        );
    }

    @Test
    void readRefreshByShopperId_None_ThrowsTokenNotFoundException() {
        User user1 = new User();
        Token token1 = new Token("jwt1", user1);
        Token token2 = new Token("jwt2", user1, TokenType.REFRESH);
        Token token3 = new Token("jwt3", user1);
        token3.setRevoked(true);
        token3.setExpired(true);
        Token token4 = new Token("jwt4", user1);
        when(mockTokenRepository.findRefreshTokenByUser_Id(user1.getId())).thenReturn(Optional.empty());

        var exception = assertThrows(TokenNotFoundException.class, () -> tokenService.readRefreshByUserId(user1.getId()));
        assertEquals("User - " + user1.getId() + " does not have valid refresh token",exception.getMessage());
    }

    @Test
    void readByToken_NotValid_ThrowsTokenNotFoundException() {
        User user1 = new User();
        Token token1 = new Token("jwt1", user1);
        when(mockTokenRepository.findByToken(token1.getToken())).thenReturn(Optional.empty());

        var exception = assertThrows(TokenNotFoundException.class, () -> tokenService.readByToken(token1.getToken()));
        assertEquals("Token does not exist - " + token1.getToken(), exception.getMessage());
    }

    @Test
    void readByToken_Valid_ReturnsToken() throws ExecutionException, InterruptedException {
        User user1 = new User();
        Token token1 = new Token("jwt1", user1);
        when(mockTokenRepository.findByToken(token1.getToken())).thenReturn(Optional.of(token1));

        var _result = tokenService.readByToken(token1.getToken());
        var result = _result.get();

        assertEquals(token1, result);
    }

    @Test
    void revokeAccessToken_NotValid_ThrowsTokenNotFoundException() {
        User user1 = new User();
        Token token1 = new Token("jwt1", user1);
        when(mockTokenRepository.findByToken(token1.getToken())).thenReturn(Optional.empty());

        var exception = assertThrows(TokenNotFoundException.class, () -> tokenService.revokeAccessToken(token1.getToken()));
        assertEquals("Token does not exist - " + token1.getToken(), exception.getMessage());
    }

    @Test
    void revokeAccessToken_Valid_InvalidatesAccessToken() {
        User user1 = new User();
        Token token1 = new Token("jwt1", user1);
        when(mockTokenRepository.findByToken(token1.getToken())).thenReturn(Optional.of(token1));

        tokenService.revokeAccessToken(token1.getToken());

        assertTrue(token1.isExpired());
        assertTrue(token1.isRevoked());
    }

    @Test
    void revokeAllTokens() {
        User user1 = new User();
        Token token1 = new Token("jwt1", user1);
        Token token2 = new Token("jwt2", user1, TokenType.REFRESH);
        Token token3 = new Token("jwt3", user1, TokenType.REFRESH);
        Token token4 = new Token("jwt4", user1);
        when(mockTokenRepository.findAllByUser_Id(user1.getId())).thenReturn(Arrays.asList(token1, token2, token3, token4));

        tokenService.revokeAllUserTokens(user1.getId());

        assertAll(
                () -> assertTrue(token1.isExpired() && token1.isRevoked()),
                () -> assertTrue(token2.isExpired() && token2.isRevoked()),
                () -> assertTrue(token3.isExpired() && token3.isRevoked()),
                () -> assertTrue(token4.isExpired() && token4.isRevoked())
        );
    }
}