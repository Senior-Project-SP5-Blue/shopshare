package com.sp5blue.shopshare.services.shoppergroup;

import com.sp5blue.shopshare.dtos.shoppergroup.ShopperGroupDto;
import com.sp5blue.shopshare.dtos.shoppergroup.SlimShopperGroupDto;
import com.sp5blue.shopshare.dtos.user.UserDto;
import com.sp5blue.shopshare.exceptions.shoppergroup.GroupNotFoundException;
import com.sp5blue.shopshare.exceptions.shoppergroup.InvalidUserPermissionsException;
import com.sp5blue.shopshare.exceptions.shoppergroup.RemoveGroupAdminException;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.models.user.User;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IShopperGroupService {
  CompletableFuture<SlimShopperGroupDto> addShopperGroup(UUID adminId, String groupName);

  CompletableFuture<ShopperGroup> createShopperGroup(UUID adminId, String groupName);

  CompletableFuture<List<SlimShopperGroupDto>> getShopperGroups(UUID userId);

  CompletableFuture<ShopperGroup> findShopperGroupById(UUID groupId);

  CompletableFuture<List<ShopperGroup>> readShopperGroups(UUID userId);

  CompletableFuture<ShopperGroupDto> getShopperGroupById(UUID userId, UUID groupId)
      throws GroupNotFoundException;

  CompletableFuture<ShopperGroup> readShopperGroupById(UUID userId, UUID groupId)
      throws GroupNotFoundException;

  void deleteShopperGroup(UUID userId, UUID groupId)
      throws GroupNotFoundException, InvalidUserPermissionsException;

  CompletableFuture<SlimShopperGroupDto> changeShopperGroupName(
      UUID userId, UUID groupId, String newName);

  CompletableFuture<ShopperGroup> updateShopperGroupName(UUID userId, UUID groupId, String newName);

  CompletableFuture<Boolean> addUserToShopperGroup(UUID groupId, UUID shopperId)
      throws GroupNotFoundException;

  CompletableFuture<Boolean> addUserToShopperGroup(UUID groupId, User user)
      throws GroupNotFoundException;

  CompletableFuture<List<UserDto>> getShopperGroupUsers(UUID userId, UUID groupId);

  CompletableFuture<UserDto> getShopperGroupUser(UUID userId, UUID groupId, UUID memberId);

  CompletableFuture<Boolean> removeUserFromShopperGroup(UUID userId, UUID groupId, UUID shopperId)
      throws GroupNotFoundException, RemoveGroupAdminException;

  CompletableFuture<Boolean> userExistsInGroup(UUID userId, UUID groupId);

  CompletableFuture<Boolean> userExistsInGroup(String username, UUID groupId);

  ShopperGroup verifyUserHasGroup(UUID userId, UUID groupId);
}
