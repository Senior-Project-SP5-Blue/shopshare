package com.sp5blue.shopshare.services.shoppergroup;

import com.sp5blue.shopshare.exceptions.shoppergroup.GroupNotFoundException;
import com.sp5blue.shopshare.exceptions.shoppergroup.InvalidUserPermissionsException;
import com.sp5blue.shopshare.exceptions.shoppergroup.RemoveGroupAdminException;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;

import java.util.List;
import java.util.UUID;

public interface IShopperGroupService {
    ShopperGroup createShopperGroup(UUID adminId, String groupName);

    List<ShopperGroup> getShopperGroups(UUID userId);

    ShopperGroup getShopperGroupById(UUID userId, UUID groupId) throws GroupNotFoundException;

    void deleteShopperGroup(UUID userId, UUID groupId) throws GroupNotFoundException, InvalidUserPermissionsException;

    void changeShopperGroupName(UUID userId, UUID groupId, String newName);

    boolean addUserToShopperGroup(UUID groupId, UUID shopperId) throws GroupNotFoundException;

    boolean addUserToShopperGroup(UUID groupId, User user) throws GroupNotFoundException;

    List<User> getShopperGroupUsers(UUID userId, UUID groupId);

    User getShopperGroupUser(UUID userId, UUID groupId, UUID memberId);

    boolean removeUserFromShopperGroup(UUID userId, UUID groupId, UUID shopperId) throws GroupNotFoundException, RemoveGroupAdminException;

    ShopperGroup verifyUserHasGroup(UUID userId, UUID groupId);
}
