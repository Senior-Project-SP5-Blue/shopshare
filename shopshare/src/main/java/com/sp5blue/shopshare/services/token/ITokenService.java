package com.sp5blue.shopshare.services.token;

import com.sp5blue.shopshare.exceptions.token.TokenNotFoundException;
import com.sp5blue.shopshare.models.Token;

import java.util.List;
import java.util.UUID;

public interface ITokenService {
    Token create(Token token);

    List<Token> create(List<Token> tokens);
    List<Token> readAllByShopperId(UUID shopperId, boolean validOnly);

    List<Token> readAllAccessByShopperId(UUID shopperId, boolean validOnly);

    Token readRefreshByShopperId(UUID shopperId) throws TokenNotFoundException ;

    Token readByToken(String token) throws TokenNotFoundException;

    void revokeAccessToken(String jwt) throws TokenNotFoundException;

    void revokeAllUserTokens(UUID shopperId);
}
