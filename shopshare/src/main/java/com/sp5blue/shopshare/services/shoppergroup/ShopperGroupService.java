package com.sp5blue.shopshare.services.shoppergroup;

import com.sp5blue.shopshare.exceptions.shoppergroup.GroupNotFoundException;
import com.sp5blue.shopshare.exceptions.shoppergroup.InvalidUserPermissionsException;
import com.sp5blue.shopshare.exceptions.shoppergroup.RemoveGroupAdminException;
import com.sp5blue.shopshare.models.shopper.Shopper;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.repositories.ShopperGroupRepository;
import com.sp5blue.shopshare.services.shopper.IShopperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service

public class ShopperGroupService implements IShopperGroupService {
    private final ShopperGroupRepository shopperGroupRepository;
    private final IShopperService shopperService;

    private final Logger logger = LoggerFactory.getLogger(ShopperGroupService.class);

    @Autowired
    public ShopperGroupService(ShopperGroupRepository shopperGroupRepository, IShopperService shopperService) {
        this.shopperGroupRepository = shopperGroupRepository;
        this.shopperService = shopperService;
    }

    @Override
    @Transactional
    public ShopperGroup createShopperGroup(UUID adminId, String groupName) {
        Shopper shopper = shopperService.getShopperById(adminId);
        ShopperGroup shopperGroup = new ShopperGroup(groupName, shopper);
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
        Shopper shopper = shopperService.getShopperById(userId);
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
        Shopper shopper = shopperService.getShopperById(shopperId);
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


    private boolean userIsAdmin(UUID userId, UUID groupId) throws InvalidUserPermissionsException {
        return shopperService.shopperExistsAsAdminByGroup(userId, groupId);
    }

    @Override
    public void verifyUserHasGroup(UUID userId, UUID groupId) {
        shopperGroupRepository.findByShopperIdAndId(userId, groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
    }
}
