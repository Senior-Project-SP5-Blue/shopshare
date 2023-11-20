package com.sp5blue.shopshare.events;

import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.services.mail.IMailService;
import com.sp5blue.shopshare.services.security.JwtService;
import com.sp5blue.shopshare.services.token.ITokenService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.concurrent.CompletableFuture;

@Component
public class InvitationListener {
    private final IMailService mailService;

    private final JwtService jwtService;

    private final ITokenService tokenService;

    @Value("${api-prefix}")
    private String apiPrefix;

//    @Value("${domain}")
//    private String domain;

    private final Logger logger = LoggerFactory.getLogger(InvitationListener.class);

    @Autowired
    public InvitationListener(IMailService mailService, JwtService jwtService, ITokenService tokenService) {
        this.mailService = mailService;
        this.jwtService = jwtService;
        this.tokenService = tokenService;
    }

    @Async
    @TransactionalEventListener(value = OnInvitationCompleteEvent.class, phase = TransactionPhase.BEFORE_COMMIT)
    public CompletableFuture<Void> onApplicationEvent(@NonNull OnInvitationCompleteEvent event) {
        User user = event.getUser();
        ShopperGroup group = event.getGroup();

//        TO-DO maybe: implement email invitation
//        String invitationJwt = jwtService.generateInvitationToken(user, group);
//        Token confirmationToken = new Token(invitationJwt, user, TokenType.INVITATION);
//        tokenService.createOrSave(confirmationToken).join();

        String recipientAddress = user.getEmail();
        String subject = "Group Invitation";

//        TO-DO: implement token
//        String acceptInviteUrl = String.format("%s%s/users/%s/groups?invite=%s", domain, apiPrefix, user.getId(), invitationJwt);

//        String acceptInviteUrl = String.format("%s%s/users/%s/groups/%s", domain, apiPrefix, user.getId(), group.getId());
        String _htmlMessage = """
                <h0>You have been invited to the group <em>%s</em>!</h0>
                """;

        String htmlMessage = String.format(_htmlMessage, group.getName());

        try {
            mailService.sendHTMLMessage(recipientAddress, subject, htmlMessage);
        } catch (MessagingException messagingException) {
            messagingException.printStackTrace();
            logger.error(messagingException.getMessage());
        }
        return null;
    }
}
