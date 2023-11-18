package com.sp5blue.shopshare.services.shoppergroup;

import com.sp5blue.shopshare.dtos.shoppergroup.ShopperGroupDto;
import com.sp5blue.shopshare.dtos.user.UserDto;
import com.sp5blue.shopshare.exceptions.shoppergroup.GroupNotFoundException;
import com.sp5blue.shopshare.exceptions.shoppergroup.InvalidUserPermissionsException;
import com.sp5blue.shopshare.exceptions.shoppergroup.RemoveGroupAdminException;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.repositories.ShopperGroupRepository;
import com.sp5blue.shopshare.services.user.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service

public class ShopperGroupService implements IShopperGroupService {
    private final ShopperGroupRepository shopperGroupRepository;
    private final IUserService userService;

    private final Logger logger = LoggerFactory.getLogger(ShopperGroupService.class);

    @Autowired
    public ShopperGroupService(ShopperGroupRepository shopperGroupRepository, IUserService userService) {
        this.shopperGroupRepository = shopperGroupRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<ShopperGroupDto> addShopperGroup(UUID adminId, String groupName) {
        User user = userService.getUserById(adminId).join();
        ShopperGroup shopperGroup = new ShopperGroup(groupName, user);
        var addedGroup = shopperGroupRepository.save(shopperGroup);
        return CompletableFuture.completedFuture(new ShopperGroupDto(addedGroup));
    }
    @Override
    @Transactional
    @Async
    public CompletableFuture<ShopperGroup> createShopperGroup(UUID adminId, String groupName) {
        User user = userService.getUserById(adminId).join();
        ShopperGroup shopperGroup = new ShopperGroup(groupName, user);
        var addedGroup = shopperGroupRepository.save(shopperGroup);
        return CompletableFuture.completedFuture(addedGroup);
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<List<ShopperGroupDto>> getShopperGroups(UUID userId) {
        var _groups = shopperGroupRepository.findAllByUserId(userId);
        var groups = _groups.stream().map(ShopperGroupDto::new).toList();
        return CompletableFuture.completedFuture(groups);
    }

    @Override
    @Async
    public CompletableFuture<ShopperGroup> findShopperGroupById(UUID groupId) {
        var group = shopperGroupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        return CompletableFuture.completedFuture(group);
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<List<ShopperGroup>> readShopperGroups(UUID userId) {
        return CompletableFuture.completedFuture(shopperGroupRepository.findAllByUserId(userId));
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<ShopperGroupDto> getShopperGroupById(UUID userId, UUID groupId) throws GroupNotFoundException {
        var group = shopperGroupRepository.findByUserIdAndId(userId, groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        return CompletableFuture.completedFuture(new ShopperGroupDto(group));
    }
    @Override
    @Async
    @Transactional
    public CompletableFuture<ShopperGroup> readShopperGroupById(UUID userId, UUID groupId) throws GroupNotFoundException {
        var group = shopperGroupRepository.findByUserIdAndId(userId, groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        return CompletableFuture.completedFuture(group);
    }

    @Override
    @Transactional
    @Async
    public void deleteShopperGroup(UUID userId, UUID groupId) throws GroupNotFoundException, InvalidUserPermissionsException {
        ShopperGroup shopperGroup = shopperGroupRepository.findByUserIdAndId(userId, groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        if (userIsAdmin(userId, groupId)) {
            shopperGroup.removeAllUsers();
            shopperGroupRepository.delete(shopperGroup);
            return;
        }
        User user = userService.getUserById(userId).join();
        shopperGroup.removeUser(user);
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<ShopperGroupDto> changeShopperGroupName(UUID userId, UUID groupId, String newName) {
        ShopperGroup shopperGroup = shopperGroupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        if (!userIsAdmin(userId, groupId)) throw new InvalidUserPermissionsException("User - " + userId + " does not have permission to modify group");
        shopperGroup.setName(newName);
        return CompletableFuture.completedFuture(new ShopperGroupDto(shopperGroup));
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<ShopperGroup> updateShopperGroupName(UUID userId, UUID groupId, String newName) {
        ShopperGroup shopperGroup = shopperGroupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        if (!userIsAdmin(userId, groupId)) throw new InvalidUserPermissionsException("User - " + userId + " does not have permission to modify group");
        shopperGroup.setName(newName);
        return CompletableFuture.completedFuture(shopperGroup);
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<Boolean> addUserToShopperGroup(UUID groupId, UUID shopperId) throws GroupNotFoundException {
        ShopperGroup group = shopperGroupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        User user = userService.getUserById(shopperId).join();
        return CompletableFuture.completedFuture(group.addUser(user));
    }
    
    @Override
    @Transactional
    @Async
    public CompletableFuture<Boolean> addUserToShopperGroup(UUID groupId, User user) throws GroupNotFoundException {
        ShopperGroup group = shopperGroupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        return CompletableFuture.completedFuture(group.addUser(user));
    }

    @Override
    @Async
    @Transactional
    public CompletableFuture<List<UserDto>> getShopperGroupUsers(UUID userId, UUID groupId) {
        verifyUserHasGroup(userId, groupId);
        var _users = userService.getUsersByShopperGroup(groupId).join();
        var users = _users.stream().map(UserDto::new).toList();
        return CompletableFuture.completedFuture(users);
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<UserDto> getShopperGroupUser(UUID userId, UUID groupId, UUID memberId) {
        verifyUserHasGroup(userId, groupId);
        return CompletableFuture.completedFuture(new UserDto(userService.getUserByShopperGroup(groupId, memberId).join()));
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<Boolean> removeUserFromShopperGroup(UUID userId, UUID groupId, UUID shopperId) throws GroupNotFoundException, RemoveGroupAdminException {
        boolean shopperIsAdmin = userIsAdmin(shopperId, groupId);
        if (shopperIsAdmin) throw new RemoveGroupAdminException("Cannot remove group admin.");
        ShopperGroup shopperGroup = shopperGroupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        if (userId.equals(shopperId)) {
            return CompletableFuture.completedFuture(shopperGroup.removeUser(shopperId));
        }
        if (!shopperGroup.getAdmin().getId().equals(userId)) throw new InvalidUserPermissionsException("User - " + userId + " does not have permission to remove from group");
        return CompletableFuture.completedFuture(shopperGroup.removeUser(shopperId));
    }

    @Override
    @Async
    public CompletableFuture<Boolean> userExistsInGroup(UUID userId, UUID groupId) {
        return CompletableFuture.completedFuture(userService.userExistsByGroup(userId, groupId).join());
    }


    private boolean userIsAdmin(UUID userId, UUID groupId) throws InvalidUserPermissionsException {
        return userService.userExistsAsAdminByGroup(userId, groupId).join();
    }

    @Override
    public ShopperGroup verifyUserHasGroup(UUID userId, UUID groupId) {
        return shopperGroupRepository.findByUserIdAndId(userId, groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
    }
}
