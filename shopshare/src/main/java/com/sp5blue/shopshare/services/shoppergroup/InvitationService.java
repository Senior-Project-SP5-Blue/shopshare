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
    public boolean invite(UUID groupId, UUID shopperId) throws UserAlreadyInvitedException {
        int rowsChanged;
        boolean alreadyInvited = (boolean) entityManager.createNativeQuery("SELECT COUNT(*) > 0 FROM group_invitations WHERE shopper_group_id = :groupId AND shopper_id = :shopperId")
                .setParameter("groupId", groupId)
                .setParameter("shopperId", shopperId)
                .getSingleResult();
        if (alreadyInvited) throw new UserAlreadyInvitedException(String.format("User - %s already has active invitation to Group - %s", shopperId, groupId));
        try {
            Query query = entityManager.createNativeQuery("INSERT INTO group_invitations(shopper_group_id, shopper_id) VALUES (:groupId, :shopperId)");
            rowsChanged = query.setParameter("groupId", groupId).setParameter("shopperId", shopperId).executeUpdate();
        } catch (Exception exception) {
            return false;
        }

        return rowsChanged == 1;
    }

    @Override
    @Transactional
    public boolean acceptInvite(UUID groupId, UUID shopperId) {
        boolean hasInvite = (boolean) entityManager.createNativeQuery("SELECT COUNT(*) > 0 FROM group_invitations WHERE shopper_group_id = :groupId AND shopper_id = :shopperId")
                .setParameter("groupId", groupId)
                .setParameter("shopperId", shopperId)
                .getSingleResult();
        if (!hasInvite) throw new UserNotInvitedException(String.format("User - %s has not been invited to Group - %s", shopperId, groupId));

        boolean added = shopperGroupService.addShopperToGroup(groupId, shopperId);
        int rowsChanged = entityManager.createNativeQuery("DELETE FROM group_invitations WHERE shopper_group_id = :groupId AND shopper_id = :shopperId")
                .setParameter("groupId", groupId)
                .setParameter("shopperId", shopperId)
                .executeUpdate();
        return added && rowsChanged == 1;
    }


}
