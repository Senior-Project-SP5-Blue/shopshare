package com.sp5blue.shopshare.services.token;

import com.sp5blue.shopshare.exceptions.token.TokenNotFoundException;
import com.sp5blue.shopshare.models.user.Token;
import com.sp5blue.shopshare.repositories.TokenRepository;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenService implements ITokenService {

  final TokenRepository tokenRepository;

  @Autowired
  public TokenService(TokenRepository tokenRepository) {
    this.tokenRepository = tokenRepository;
  }

  @Override
  @Transactional
  @Async
  public CompletableFuture<Token> createOrSave(Token token) {
    return CompletableFuture.completedFuture(tokenRepository.save(token));
  }

  @Override
  @Transactional
  @Async
  public CompletableFuture<List<Token>> createOrSave(Token[] tokens) {
    return CompletableFuture.completedFuture(tokenRepository.saveAll(Arrays.asList(tokens)));
  }

  @Override
  @Async
  public CompletableFuture<List<Token>> readAllByUserId(UUID shopperId, boolean validOnly) {
    if (!validOnly)
      return CompletableFuture.completedFuture(tokenRepository.findAllByUser_Id(shopperId));
    return CompletableFuture.completedFuture(
        tokenRepository.findAllValidTokensByUser_Id(shopperId));
  }

  @Override
  @Async
  public CompletableFuture<List<Token>> readAllAccessByUserId(UUID shopperId, boolean validOnly) {
    if (!validOnly)
      return CompletableFuture.completedFuture(
          tokenRepository.findAllAccessTokensByUser_Id(shopperId));
    return CompletableFuture.completedFuture(
        tokenRepository.findAllValidAccessTokensByUser_Id(shopperId));
  }

  @Override
  @Async
  public CompletableFuture<Token> readRefreshByUserId(UUID shopperId)
      throws TokenNotFoundException {
    return CompletableFuture.completedFuture(
        tokenRepository
            .findRefreshTokenByUser_Id(shopperId)
            .orElseThrow(
                () ->
                    new TokenNotFoundException(
                        "User - " + shopperId + " does not have valid refresh token")));
  }

  @Override
  @Async
  public CompletableFuture<Token> readByToken(String token) throws TokenNotFoundException {
    return CompletableFuture.completedFuture(
        tokenRepository
            .findByToken(token)
            .orElseThrow(() -> new TokenNotFoundException("Invalid Token")));
  }

  @Override
  @Async
  public CompletableFuture<Token> readByConfirmationToken(String token)
      throws TokenNotFoundException {
    return CompletableFuture.completedFuture(
        tokenRepository
            .findByConfirmationToken(token)
            .orElseThrow(() -> new TokenNotFoundException("Invalid Token")));
  }

  @Override
  @Async
  public CompletableFuture<Token> readByInvitationToken(String token)
      throws TokenNotFoundException {
    return CompletableFuture.completedFuture(
        tokenRepository
            .findByInvitationToken(token)
            .orElseThrow(() -> new TokenNotFoundException("Invalid Token")));
  }

  @Override
  @Transactional
  @Async
  public CompletableFuture<Void> revokeAccessToken(String jwt) {
    Token storedToken =
        tokenRepository
            .findByToken(jwt)
            .orElseThrow(() -> new TokenNotFoundException("Invalid Token"));
    storedToken.setExpired(true);
    storedToken.setRevoked(true);
    return null;
  }

  @Override
  @Transactional
  @Async
  public CompletableFuture<Void> revokeAllUserTokens(UUID userId) {
    tokenRepository.revokeTokensByUser(userId);
    return null;
  }

  //    @Override
  //    @Transactional
  //    @Async
  //    public CompletableFuture<Void> deleteExpiredTokens() {
  //        tokenRepository.deleteExpiredTokens();
  //        return null;
  //    }
}
