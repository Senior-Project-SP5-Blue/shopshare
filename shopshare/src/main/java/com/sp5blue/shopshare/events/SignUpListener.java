package com.sp5blue.shopshare.events;

import com.sp5blue.shopshare.models.user.Token;
import com.sp5blue.shopshare.models.user.TokenType;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.services.mail.IMailService;
import com.sp5blue.shopshare.services.security.JwtService;
import com.sp5blue.shopshare.services.token.ITokenService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SignUpListener implements ApplicationListener<OnSignUpCompleteEvent> {
  private final JwtService jwtService;

  private final ITokenService tokenService;

  private final IMailService mailService;

  @Value("${API_URL}")
  private String API_URL;

  private final Logger logger = LoggerFactory.getLogger(SignUpListener.class);

  public SignUpListener(
      JwtService jwtService, ITokenService tokenService, IMailService mailService) {
    this.jwtService = jwtService;
    this.tokenService = tokenService;
    this.mailService = mailService;
  }

  @Override
  public void onApplicationEvent(@NonNull OnSignUpCompleteEvent event) {
    this.sendConfirmSignUp(event);
  }

  @Async
  public void sendConfirmSignUp(OnSignUpCompleteEvent event) {
    User user = event.getUser();
    final String confirmJwt = jwtService.generateConfirmationToken(user);
    Token confirmationToken = new Token(confirmJwt, user, TokenType.CONFIRMATION);
    tokenService.createOrSave(confirmationToken).join();

    String recipientAddress = user.getEmail();
    String subject = "Registration Confirmation";
    String confirmationUrl = API_URL + "/auth/confirm-signup?token=" + confirmationToken.getToken();

    String htmlMessage =
        """
                <h0>Please click the link to <a href="%s">Verify Email</a></h0>
                """;
    logger.warn("Handler running in thread: {}", Thread.currentThread());
    try {
      mailService.sendHTMLMessage(
          recipientAddress, subject, String.format(htmlMessage, confirmationUrl));
    } catch (MessagingException messagingException) {
      messagingException.printStackTrace();
      logger.error(messagingException.getMessage());
    }
  }
}
