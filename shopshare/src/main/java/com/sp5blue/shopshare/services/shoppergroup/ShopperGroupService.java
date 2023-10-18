package com.sp5blue.shopshare.services.shoppergroup;

import com.sp5blue.shopshare.exceptions.shoppergroup.GroupNotFoundException;
import com.sp5blue.shopshare.exceptions.shoppergroup.InvalidUserPermissionsException;
import com.sp5blue.shopshare.exceptions.shoppergroup.RemoveGroupAdminException;
import com.sp5blue.shopshare.models.shopper.Role;
import com.sp5blue.shopshare.models.shopper.RoleType;
import com.sp5blue.shopshare.models.shopper.Shopper;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.repositories.ShopperGroupRepository;
import com.sp5blue.shopshare.services.shopper.IShopperService;
import com.sp5blue.shopshare.services.shopper.ShopperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service

public class ShopperGroupService implements IShopperGroupService {
    private final ShopperGroupRepository shopperGroupRepository;
    private final IShopperService shopperService;

    private final Logger logger = LoggerFactory.getLogger(ShopperGroupService.class);

    @Autowired
    public ShopperGroupService(ShopperGroupRepository shopperGroupRepository, ShopperService shopperService) {
        this.shopperGroupRepository = shopperGroupRepository;
        this.shopperService = shopperService;
    }

    @Override
    @Transactional
    public ShopperGroup createShopperGroup(Shopper admin, String shopperGroupName) {
        ShopperGroup shopperGroup = new ShopperGroup(shopperGroupName, admin);
        Role role = new Role("ROLE_GROUP_ADMIN-" + shopperGroup.getId(), RoleType.ROLE_GROUP_ADMIN);
        admin.addRole(role);
        return shopperGroupRepository.save(shopperGroup);
    }

    @Override
    @Transactional
    public ShopperGroup createShopperGroup(ShopperGroup shopperGroup) {
        Role role = new Role("ROLE_GROUP_ADMIN-" + shopperGroup.getId(), RoleType.ROLE_GROUP_ADMIN);
        shopperGroup.getAdmin().addRole(role);
        return shopperGroupRepository.save(shopperGroup);
    }

    @Override
    @Transactional
    public ShopperGroup createShopperGroup(UUID creatorId, String groupName) {
        Shopper shopper = shopperService.readShopperById(creatorId);
        ShopperGroup shopperGroup = new ShopperGroup(groupName, shopper);
        Role role = new Role("ROLE_GROUP_ADMIN-" + shopperGroup.getId(), RoleType.ROLE_GROUP_ADMIN);
        shopper.addRole(role);
        return shopperGroupRepository.save(shopperGroup);
    }

    @Override
    public List<ShopperGroup> getShopperGroupsByShopper(UUID shopperId) {
        return shopperGroupRepository.findAllByShopperId(shopperId);
    }

    @Override
    public List<ShopperGroup> getShopperGroupsByShopper(Shopper shopper) {
        return shopper.getGroups();
    }

    @Override
    public ShopperGroup getShopperGroupById(UUID groupId) throws GroupNotFoundException {
        return shopperGroupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
    }
    @Override
    public ShopperGroup getShopperGroupByShopperAndId(UUID shopperId, UUID groupId) throws GroupNotFoundException {
        return shopperGroupRepository.findByShopperIdAndId(shopperId, groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
    }

    @Override
    public ShopperGroup getShopperGroupByShopperAndId(Shopper shopper, UUID groupId) throws GroupNotFoundException {
        return shopper.getGroups().stream().filter(g -> g.getId().equals(groupId)).findFirst().orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
    }

    @Override
    public List<ShopperGroup> getShopperGroupsByName(String name) {
        return shopperGroupRepository.findAllByName(name);
    }

    @Override
    public List<ShopperGroup> getShopperGroupsByShopperAndName(UUID shopperId, String name) {
        return shopperGroupRepository.findAllByShopperIdAndName(shopperId, name);
    }

    @Override
    public List<ShopperGroup> getShopperGroupsByShopperAndName(Shopper shopper, String name) {
        return shopper.getGroups().stream().filter(g -> g.getName().equals(name)).collect(Collectors.toList());
    }

    @Override
    public List<ShopperGroup> getShopperGroupsByAdmin(UUID shopperId) {
        return shopperGroupRepository.findAllByAdmin_Id(shopperId);
    }

    @Override
    public List<ShopperGroup> getShopperGroups() {
        return shopperGroupRepository.findAll();
    }

    @Override
    public boolean shopperGroupExistsById(UUID groupId) {
        return shopperGroupRepository.existsById(groupId);
    }

    @Override
    @Transactional
    public void deleteShopperGroup(UUID shopperId, UUID groupId) throws GroupNotFoundException, InvalidUserPermissionsException {
        ShopperGroup shopperGroup = shopperGroupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        Shopper shopper = shopperService.readShopperById(shopperId);
        if (!shopperGroup.getAdmin().equals(shopper)) throw new InvalidUserPermissionsException("User - " + shopperId + " does not have permission to delete group");
        shopperGroupRepository.delete(shopperGroup);
    }

    @Override
    @Transactional
    public void changeShopperGroupName(UUID groupId, String newName) {
        ShopperGroup shopperGroup = shopperGroupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
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
    public boolean removeShopperFromShopperGroup(UUID groupId, UUID shopperId) throws GroupNotFoundException, RemoveGroupAdminException {
        ShopperGroup group = shopperGroupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        if (shopperId == group.getAdmin().getId()) throw new RemoveGroupAdminException("Cannot remove group admin.");
        return group.removeShopper(shopperId);
    }
    @Override
    @Transactional
    public boolean removeShopperFromShopperGroup(UUID groupId, Shopper shopper) throws GroupNotFoundException {
        ShopperGroup group = shopperGroupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId));
        if (shopper.equals(group.getAdmin())) throw new RemoveGroupAdminException("Cannot remove group admin.");
        return group.removeShopper(shopper);
    }
}
