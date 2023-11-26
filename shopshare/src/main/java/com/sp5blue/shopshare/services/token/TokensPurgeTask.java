package com.sp5blue.shopshare.services.token;

import com.sp5blue.shopshare.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokensPurgeTask {
  private final TokenRepository tokenRepository;

  @Autowired
  public TokensPurgeTask(TokenRepository tokenRepository) {
    this.tokenRepository = tokenRepository;
  }

  @Scheduled(cron = "@daily")
  @Transactional
  public void purgeExpired() {
    tokenRepository.deleteExpiredTokens();
  }
}
