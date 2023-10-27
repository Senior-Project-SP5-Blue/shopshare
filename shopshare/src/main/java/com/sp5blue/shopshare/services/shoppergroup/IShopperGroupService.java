package com.sp5blue.shopshare.services.shoppergroup;

import com.sp5blue.shopshare.exceptions.shoppergroup.GroupNotFoundException;
import com.sp5blue.shopshare.exceptions.shoppergroup.InvalidUserPermissionsException;
import com.sp5blue.shopshare.exceptions.shoppergroup.RemoveGroupAdminException;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IShopperGroupService {
    CompletableFuture<ShopperGroup> createShopperGroup(UUID adminId, String groupName);

    CompletableFuture<List<ShopperGroup>> getShopperGroups(UUID userId);

    CompletableFuture<ShopperGroup> getShopperGroupById(UUID userId, UUID groupId) throws GroupNotFoundException;

    void deleteShopperGroup(UUID userId, UUID groupId) throws GroupNotFoundException, InvalidUserPermissionsException;

    void changeShopperGroupName(UUID userId, UUID groupId, String newName);

    CompletableFuture<Boolean> addUserToShopperGroup(UUID groupId, UUID shopperId) throws GroupNotFoundException;

    CompletableFuture<Boolean> addUserToShopperGroup(UUID groupId, User user) throws GroupNotFoundException;

    CompletableFuture<List<User>> getShopperGroupUsers(UUID userId, UUID groupId);

    CompletableFuture<User> getShopperGroupUser(UUID userId, UUID groupId, UUID memberId);

    CompletableFuture<Boolean> removeUserFromShopperGroup(UUID userId, UUID groupId, UUID shopperId) throws GroupNotFoundException, RemoveGroupAdminException;

    ShopperGroup verifyUserHasGroup(UUID userId, UUID groupId);
}
