package com.sp5blue.shopshare.services.token;

import com.sp5blue.shopshare.exceptions.token.TokenNotFoundException;
import com.sp5blue.shopshare.models.user.Token;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ITokenService {
    CompletableFuture<Token> createOrSave(Token token);

    CompletableFuture<List<Token>> createOrSave(Token[] tokens);

    CompletableFuture<List<Token>> readAllByUserId(UUID shopperId, boolean validOnly);

    CompletableFuture<List<Token>> readAllAccessByUserId(UUID shopperId, boolean validOnly);

    CompletableFuture<Token> readRefreshByUserId(UUID shopperId) throws TokenNotFoundException ;

    CompletableFuture<Token> readByToken(String token) throws TokenNotFoundException;

    CompletableFuture<Token> readByConfirmationToken(String token) throws TokenNotFoundException;

    void revokeAccessToken(String jwt) throws TokenNotFoundException;

    void revokeAllUserTokens(UUID shopperId);
}
