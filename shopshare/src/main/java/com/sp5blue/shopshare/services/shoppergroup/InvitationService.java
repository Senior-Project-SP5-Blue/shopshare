package com.sp5blue.shopshare.services.shoppergroup;

import com.sp5blue.shopshare.exceptions.shoppergroup.UserAlreadyInGroupException;
import com.sp5blue.shopshare.exceptions.shoppergroup.UserNotInvitedException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class InvitationService implements IInvitationService {
    @PersistenceContext
    EntityManager entityManager;

    private final IShopperGroupService shopperGroupService;

    @Autowired
    public InvitationService(IShopperGroupService shopperGroupService) {
        this.shopperGroupService = shopperGroupService;
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<Boolean> invite(UUID groupId, UUID userId) throws UserAlreadyInGroupException {
        boolean userInGroup = shopperGroupService.userExistsInGroup(userId, groupId).join();

        if (userInGroup) throw new UserAlreadyInGroupException("User is already a member of group");

        try {
            entityManager.createNativeQuery("INSERT INTO group_invitations(shopper_group_id, user_id) VALUES (:group_id, :user_id)")
                .setParameter("group_id", groupId)
                .setParameter("user_id", userId)
                .executeUpdate();
        } catch (Exception exception) {
            return CompletableFuture.completedFuture(false);
        }
        return CompletableFuture.completedFuture(true);
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<Boolean> acceptInvite(UUID groupId, UUID userId) {
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
            CompletableFuture.completedFuture(false);
        }
        return CompletableFuture.completedFuture(true);
    }
}
