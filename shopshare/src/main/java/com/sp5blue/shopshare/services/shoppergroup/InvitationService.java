package com.sp5blue.shopshare.services.shoppergroup;

import com.sp5blue.shopshare.exceptions.shoppergroup.UserAlreadyInvitedException;
import com.sp5blue.shopshare.exceptions.shoppergroup.UserNotInvitedException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
    public boolean invite(UUID groupId, UUID userId) throws UserAlreadyInvitedException {
        int rowsChanged;
        boolean alreadyInvited = (boolean) entityManager.createNativeQuery("SELECT COUNT(*) > 0 FROM group_invitations WHERE shopper_group_id = :group_id AND user_id = :user_id")
                .setParameter("group_i", groupId)
                .setParameter("user_id", userId)
                .getSingleResult();
         Boolean alreadyInGroup = (Integer) entityManager.createNativeQuery("""
SELECT 1 FROM groups WHERE shopper_group_id = :groupId AND user_id = :userId
""").setParameter("groupId", groupId)
.setParameter("userId", userId)
.getSingleResult() == 1 ? true : false;

        if (alreadyInvited || alreadyInGroup) throw new UserAlreadyInvitedException(String.format("User - %s already has active invitation to Group - %s", userId, groupId));
        try {
            Query query = entityManager.createNativeQuery("INSERT INTO group_invitations(shopper_group_id, user_id) VALUES (:group_id, :user_id)");
            rowsChanged = query.setParameter("group_id", groupId).setParameter("user_id", userId).executeUpdate();
        } catch (Exception exception) {
            return false;
        }

        return rowsChanged == 1;
    }

    @Override
    @Transactional
    public boolean acceptInvite(UUID groupId, UUID userId) {
        boolean hasInvite = (boolean) entityManager.createNativeQuery("SELECT COUNT(*) > 0 FROM group_invitations WHERE shopper_group_id = :groupId AND user_id = :userId")
                .setParameter("groupId", groupId)
                .setParameter("userId", userId)
                .getSingleResult();
        if (!hasInvite) throw new UserNotInvitedException(String.format("User - %s has not been invited to Group - %s", userId, groupId));

        boolean added = shopperGroupService.addUserToShopperGroup(groupId, userId);
        int rowsChanged = entityManager.createNativeQuery("DELETE FROM group_invitations WHERE shopper_group_id = :groupId AND user_id = :userId")
                .setParameter("groupId", groupId)
                .setParameter("userId", userId)
                .executeUpdate();
        return added && rowsChanged == 1;
    }


}
