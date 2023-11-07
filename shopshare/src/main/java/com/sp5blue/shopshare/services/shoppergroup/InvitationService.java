package com.sp5blue.shopshare.services.shoppergroup;

import com.sp5blue.shopshare.events.OnInvitationCompleteEvent;
import com.sp5blue.shopshare.exceptions.shoppergroup.UserAlreadyInGroupException;
import com.sp5blue.shopshare.exceptions.shoppergroup.UserNotInvitedException;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.models.user.Token;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.services.security.JwtService;
import com.sp5blue.shopshare.services.token.ITokenService;
import com.sp5blue.shopshare.services.user.IUserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class InvitationService implements IInvitationService {
    @PersistenceContext
    EntityManager entityManager;

    private final IShopperGroupService shopperGroupService;

    private final IUserService userService;

    private final ApplicationEventPublisher eventPublisher;

    private final ITokenService tokenService;

    private final JwtService jwtService;

    @Autowired
    public InvitationService(IShopperGroupService shopperGroupService, IUserService userService, ApplicationEventPublisher eventPublisher, ITokenService tokenService, JwtService jwtService) {
        this.shopperGroupService = shopperGroupService;
        this.userService = userService;
        this.eventPublisher = eventPublisher;
        this.tokenService = tokenService;
        this.jwtService = jwtService;
    }

    @Override
    @Async
    @Transactional
    public void invite(UUID groupId, UUID userId) throws UserAlreadyInGroupException {
        boolean userInGroup = shopperGroupService.userExistsInGroup(userId, groupId).join();

        if (userInGroup) throw new UserAlreadyInGroupException("User is already a member of group");

        try {
            entityManager.createNativeQuery("INSERT INTO group_invitations(shopper_group_id, user_id) VALUES (:group_id, :user_id)")
                .setParameter("group_id", groupId)
                .setParameter("user_id", userId)
                .executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        User user = userService.getUserById(userId).join();
        ShopperGroup group = shopperGroupService.findShopperGroupById(groupId).join();
        eventPublisher.publishEvent(new OnInvitationCompleteEvent(this, user, group) );
    }

    @Override
    @Transactional
    @Async
    public void acceptInvite(UUID groupId, UUID userId) {
        boolean hasInvite = (boolean) entityManager.createNativeQuery("SELECT EXISTS (SELECT *  FROM group_invitations WHERE shopper_group_id = :groupId AND user_id = :userId)")
                .setParameter("groupId", groupId)
                .setParameter("userId", userId)
                .getSingleResult();
        if (!hasInvite) throw new UserNotInvitedException("Invalid Invitation");

        shopperGroupService.addUserToShopperGroup(groupId, userId).join();
        try {
            entityManager.createNativeQuery("DELETE FROM group_invitations WHERE shopper_group_id = :groupId AND user_id = :userId")
                    .setParameter("groupId", groupId)
                    .setParameter("userId", userId)
                    .executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    @Override
    @Transactional
    @Async
    public void acceptInvite(String inviteToken) {
        Token invitationToken = tokenService.readByInvitationToken(inviteToken).join();
        UUID userId = UUID.fromString(jwtService.extractSubject(inviteToken));
        UUID groupId = UUID.fromString(jwtService.extractSubject("group"));


        boolean hasInvite = (boolean) entityManager.createNativeQuery("SELECT EXISTS (SELECT *  FROM group_invitations WHERE shopper_group_id = :groupId AND user_id = :userId)")
                .setParameter("groupId", groupId)
                .setParameter("userId", userId)
                .getSingleResult();
        if (!hasInvite || invitationToken.isExpired()) throw new UserNotInvitedException("Invalid Invitation");

        shopperGroupService.addUserToShopperGroup(groupId, userId).join();
        try {
            entityManager.createNativeQuery("DELETE FROM group_invitations WHERE shopper_group_id = :groupId AND user_id = :userId")
                    .setParameter("groupId", groupId)
                    .setParameter("userId", userId)
                    .executeUpdate();
            invitationToken.setExpired(true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        tokenService.createOrSave(invitationToken);
    }
}
