package com.sp5blue.shopshare.services.shoppergroup;

import com.sp5blue.shopshare.exceptions.shoppergroup.GroupNotFoundException;
import com.sp5blue.shopshare.exceptions.shoppergroup.InvalidUserPermissionsException;
import com.sp5blue.shopshare.exceptions.shoppergroup.RemoveGroupAdminException;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
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
    public CompletableFuture<ShopperGroup> createShopperGroup(UUID adminId, String groupName) {
        User user = userService.getUserById(adminId).join();
        ShopperGroup shopperGroup = new ShopperGroup(groupName, user);
        return CompletableFuture.completedFuture(shopperGroupRepository.save(shopperGroup));
    }

    @Override
    @Async
    public CompletableFuture<List<ShopperGroup>> getShopperGroups(UUID userId) {
        return CompletableFuture.completedFuture(shopperGroupRepository.findAllByUserId(userId));
    }

    @Override
    @Async
    public CompletableFuture<ShopperGroup> getShopperGroupById(UUID userId, UUID groupId) throws GroupNotFoundException {
        return CompletableFuture.completedFuture(shopperGroupRepository.findByUserIdAndId(userId, groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId)));
    }

    @Override
    @Transactional
    @Async
    public void deleteShopperGroup(UUID userId, UUID groupId) throws GroupNotFoundException, InvalidUserPermissionsException {
        ShopperGroup shopperGroup = shopperGroupRepository.findByUserIdAndId(userId, groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        if (userIsAdmin(userId, groupId)) {
            shopperGroupRepository.delete(shopperGroup);
            return;
        }
        User user = userService.getUserById(userId).join();
        shopperGroup.removeUser(user);
    }

    @Override
    @Transactional
    @Async
    public void changeShopperGroupName(UUID userId, UUID groupId, String newName) {
        ShopperGroup shopperGroup = shopperGroupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        if (!userIsAdmin(userId, groupId)) throw new InvalidUserPermissionsException("User - " + userId + " does not have permission to modify group");
        shopperGroup.setName(newName);
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
    @Transactional
    @Async
    public CompletableFuture<Boolean> removeUserFromShopperGroup(UUID userId, UUID groupId, UUID shopperId) throws GroupNotFoundException, RemoveGroupAdminException {
        boolean shopperIsAdmin = userIsAdmin(shopperId, groupId);
        if (shopperIsAdmin) throw new RemoveGroupAdminException("Cannot remove group admin.");
        ShopperGroup shopperGroup;
        shopperGroup = shopperGroupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        if (userId.equals(shopperId)) {
            return CompletableFuture.completedFuture(shopperGroup.removeUser(shopperId));
        }

        if (!shopperGroup.getAdmin().getId().equals(userId)) throw new InvalidUserPermissionsException("User - " + userId + " does not have permission to remove from group");
        return CompletableFuture.completedFuture(shopperGroup.removeUser(shopperId));
    }

    private boolean userIsAdmin(UUID userId, UUID groupId) throws InvalidUserPermissionsException {
        return userService.userExistsAsAdminByGroup(userId, groupId).join();
    }

    @Override
    public void verifyUserHasGroup(UUID userId, UUID groupId) {
        shopperGroupRepository.findByUserIdAndId(userId, groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
    }
}
