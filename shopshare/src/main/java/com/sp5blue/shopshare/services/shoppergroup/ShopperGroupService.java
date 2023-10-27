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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public ShopperGroup createShopperGroup(UUID adminId, String groupName) {
        User user = userService.getUserById(adminId);
        ShopperGroup shopperGroup = new ShopperGroup(groupName, user);
        return shopperGroupRepository.save(shopperGroup);
    }

    @Override
    public List<ShopperGroup> getShopperGroups(UUID userId) {
        return shopperGroupRepository.findAllByUserId(userId);
    }

    @Override
    public ShopperGroup getShopperGroupById(UUID userId, UUID groupId) throws GroupNotFoundException {
        return shopperGroupRepository.findByUserIdAndId(userId, groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
    }

    @Override
    @Transactional
    public void deleteShopperGroup(UUID userId, UUID groupId) throws GroupNotFoundException, InvalidUserPermissionsException {
        ShopperGroup shopperGroup = shopperGroupRepository.findByUserIdAndId(userId, groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        logger.warn("Group is: {}", shopperGroup.getId());
        if (userIsAdmin(userId, groupId)) {
            logger.warn("User is admin");
            shopperGroup.setUsers(new ArrayList<>());
            shopperGroupRepository.delete(shopperGroup);
            return;
        }
        logger.warn("User is not admin");
        User user = userService.getUserById(userId);
        shopperGroup.removeUser(user);
    }

    @Override
    @Transactional
    public void changeShopperGroupName(UUID userId, UUID groupId, String newName) {
        ShopperGroup shopperGroup = shopperGroupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        if (!userIsAdmin(userId, groupId)) throw new InvalidUserPermissionsException("User - " + userId + " does not have permission to modify group");
        shopperGroup.setName(newName);
    }

    @Override
    @Transactional
    public boolean addUserToShopperGroup(UUID groupId, UUID shopperId) throws GroupNotFoundException {
        ShopperGroup group = shopperGroupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        User user = userService.getUserById(shopperId);
        return group.addUser(user);
    }
    
    @Override
    @Transactional
    public boolean addUserToShopperGroup(UUID groupId, User user) throws GroupNotFoundException {
        ShopperGroup group = shopperGroupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        return group.addUser(user);
    }

    @Override
    public List<User> getShopperGroupUsers(UUID userId, UUID groupId) {
        verifyUserHasGroup(userId, groupId);
        return userService.getUsersByShopperGroup(groupId);
    }

    @Override
    public User getShopperGroupUser(UUID userId, UUID groupId, UUID memberId) {
        verifyUserHasGroup(userId, groupId);
        return userService.getUserByShopperGroup(groupId, memberId);
    }

    @Override
    @Transactional
    public boolean removeUserFromShopperGroup(UUID userId, UUID groupId, UUID shopperId) throws GroupNotFoundException, RemoveGroupAdminException {
        boolean shopperIsAdmin = userIsAdmin(shopperId, groupId);
        if (shopperIsAdmin) throw new RemoveGroupAdminException("Cannot remove group admin.");
        ShopperGroup shopperGroup;
        shopperGroup = shopperGroupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        if (userId.equals(shopperId)) {
            return shopperGroup.removeUser(shopperId);
        }

        if (!shopperGroup.getAdmin().getId().equals(userId)) throw new InvalidUserPermissionsException("User - " + userId + " does not have permission to remove from group");
        return shopperGroup.removeUser(shopperId);
    }


    private boolean userIsAdmin(UUID userId, UUID groupId) throws InvalidUserPermissionsException {
        return userService.userExistsAsAdminByGroup(userId, groupId);
    }

    @Override
    public ShopperGroup verifyUserHasGroup(UUID userId, UUID groupId) {
        return shopperGroupRepository.findByUserIdAndId(userId, groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
    }
}
