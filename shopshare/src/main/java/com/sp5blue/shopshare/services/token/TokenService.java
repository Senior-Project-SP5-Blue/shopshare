package com.sp5blue.shopshare.services.token;

import com.sp5blue.shopshare.exceptions.token.TokenNotFoundException;
import com.sp5blue.shopshare.models.Token;
import com.sp5blue.shopshare.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TokenService implements ITokenService {

    TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional
    public Token create(Token token) {
        return tokenRepository.save(token);
    }

    @Override
    @Transactional
    public List<Token> create(List<Token> tokens) {
        return tokenRepository.saveAll(tokens);
    }

    @Override
    public List<Token> readAllByShopperId(UUID shopperId, boolean validOnly) {
        if (!validOnly) return tokenRepository.findAllByShopper_Id(shopperId);
        return tokenRepository.findAllValidTokensByShopper_Id(shopperId);
    }

    @Override
    public List<Token> readAllAccessByShopperId(UUID shopperId, boolean validOnly) {
        if (!validOnly) return tokenRepository.findAllByShopper_Id(shopperId);
        return tokenRepository.findAllValidTokensByShopper_Id(shopperId);
    }
    @Override
    public Token readRefreshByShopperId(UUID shopperId) throws TokenNotFoundException {
        return tokenRepository.findRefreshTokenByShopper_Id(shopperId).orElseThrow(() -> new TokenNotFoundException("User - " + shopperId + " does not have valid refresh token" ));
    }

    @Override
    public Token readByToken(String token) throws TokenNotFoundException {
        return tokenRepository.findByToken(token).orElseThrow(() -> new TokenNotFoundException("Token does not exist - " + token));
    }

    @Override
    @Transactional
    public void invalidateAccessToken(String jwt) {
        Token storedToken = tokenRepository.findByToken(jwt).orElseThrow(() -> new TokenNotFoundException("Token does not exist - " + jwt));
        storedToken.setExpired(true);
        storedToken.setRevoked(true);
    }

    @Override
    @Transactional
    public void revokeRefreshToken(UUID shopperId) {
        Token refreshToken = tokenRepository.findRefreshTokenByShopper_Id(shopperId).orElseThrow(() -> new TokenNotFoundException("User - " + shopperId + " does not have valid refresh token" ));
        refreshToken.setExpired(true);
        refreshToken.setRevoked(true);
        tokenRepository.delete(refreshToken);
    }
    @Override
    @Transactional
    public void revokeAllTokens(UUID shopperId) {
        List<Token> tokens = tokenRepository.findAllByShopper_Id(shopperId);
        tokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
    }
}
