package com.sp5blue.shopshare.services.shoppergroup;

import com.sp5blue.shopshare.exceptions.shoppergroup.GroupNotFoundException;
import com.sp5blue.shopshare.exceptions.shoppergroup.InvalidUserPermissionsException;
import com.sp5blue.shopshare.exceptions.shoppergroup.RemoveGroupAdminException;
import com.sp5blue.shopshare.models.shopper.Shopper;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.repositories.ShopperGroupRepository;
import com.sp5blue.shopshare.services.shopper.IShopperService1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service

public class ShopperGroupService1 implements IShopperGroupService1 {
    private final ShopperGroupRepository shopperGroupRepository;
    private final IShopperService1 shopperService;

    private final Logger logger = LoggerFactory.getLogger(ShopperGroupService1.class);

    @Autowired
    public ShopperGroupService1(ShopperGroupRepository shopperGroupRepository, IShopperService1 shopperService) {
        this.shopperGroupRepository = shopperGroupRepository;
        this.shopperService = shopperService;
    }

    @Override
    @Transactional
    public ShopperGroup createShopperGroup(UUID adminId, String groupName) {
        Shopper shopper = shopperService.readShopperById(adminId);
        ShopperGroup shopperGroup = new ShopperGroup(groupName, shopper);
//        Role role = new Role("ROLE_GROUP_ADMIN-" + shopperGroup.getId(), RoleType.ROLE_GROUP_ADMIN);
//        shopper.addRole(role);
        return shopperGroupRepository.save(shopperGroup);
    }

    @Override
    public List<ShopperGroup> getShopperGroups(UUID userId) {
        return shopperGroupRepository.findAllByShopperId(userId);
    }

    @Override
    public ShopperGroup getShopperGroupById(UUID userId, UUID groupId) throws GroupNotFoundException {
        return shopperGroupRepository.findByShopperIdAndId(userId, groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
    }

    @Override
    @Transactional
    public void deleteShopperGroup(UUID userId, UUID groupId) throws GroupNotFoundException, InvalidUserPermissionsException {
        ShopperGroup shopperGroup = shopperGroupRepository.findByShopperIdAndId(userId, groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        if (userIsAdmin(userId, groupId)) {
            shopperGroupRepository.delete(shopperGroup);
            return;
        }
        Shopper shopper = shopperService.readShopperById(userId);
        shopperGroup.removeShopper(shopper);
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
    public boolean addShopperToShopperGroup(UUID groupId, UUID shopperId) throws GroupNotFoundException {
        ShopperGroup group = shopperGroupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        Shopper shopper = shopperService.readShopperById(shopperId);
        return group.addShopper(shopper);
    }
    
    @Override
    @Transactional
    public boolean addShopperToShopperGroup(UUID groupId, Shopper shopper) throws GroupNotFoundException {
        ShopperGroup group = shopperGroupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        return group.addShopper(shopper);
    }

    @Override
    @Transactional
    public boolean removeShopperFromShopperGroup(UUID userId, UUID groupId, UUID shopperId) throws GroupNotFoundException, RemoveGroupAdminException {
        boolean shopperIsAdmin = userIsAdmin(shopperId, groupId);
        if (shopperIsAdmin) throw new RemoveGroupAdminException("Cannot remove group admin.");
        ShopperGroup shopperGroup;
        shopperGroup = shopperGroupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        if (userId.equals(shopperId)) {
            return shopperGroup.removeShopper(shopperId);
        }

        if (!shopperGroup.getAdmin().getId().equals(userId)) throw new InvalidUserPermissionsException("User - " + userId + " does not have permission to remove from group");
        return shopperGroup.removeShopper(shopperId);
    }

    private void verifyUserInGroup(UUID userId, UUID groupId) throws GroupNotFoundException, InvalidUserPermissionsException {
        boolean userIsMember = shopperService.shopperExistsByGroup(userId, groupId);
        if (!userIsMember) throw new GroupNotFoundException("Shopper group does not exist - " + groupId);
    }

    private boolean userIsAdmin(UUID userId, UUID groupId) throws InvalidUserPermissionsException {
        return shopperService.shopperExistsAsAdminByGroup(userId, groupId);
    }
}
