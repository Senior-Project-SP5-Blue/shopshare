package com.sp5blue.shopshare.services.token;

import com.sp5blue.shopshare.exceptions.token.TokenNotFoundException;
import com.sp5blue.shopshare.models.Shopper;
import com.sp5blue.shopshare.models.ShoppingList;
import com.sp5blue.shopshare.models.Token;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ITokenService {
    public Token create(Token token);

    public List<Token> create(List<Token> tokens);
    List<Token> readAllByShopperId(UUID shopperId, boolean validOnly);

    List<Token> readAllAccessByShopperId(UUID shopperId, boolean validOnly);

    Token readRefreshByShopperId(UUID shopperId) throws TokenNotFoundException ;

    Token readByToken(String token) throws TokenNotFoundException;

    void invalidateAccessToken(String jwt) throws TokenNotFoundException;

    void revokeRefreshToken(UUID shopperId);

    void revokeAllTokens(UUID shopperId);
}
