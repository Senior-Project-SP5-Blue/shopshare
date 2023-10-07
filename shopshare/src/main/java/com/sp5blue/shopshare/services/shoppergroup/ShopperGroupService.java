package com.sp5blue.shopshare.services.shoppergroup;

import com.sp5blue.shopshare.exceptions.shoppergroup.GroupNotFoundException;
import com.sp5blue.shopshare.models.Shopper;
import com.sp5blue.shopshare.models.ShopperGroup;
import com.sp5blue.shopshare.repositories.ShopperGroupRepository;
import com.sp5blue.shopshare.services.shopper.IShopperService;
import com.sp5blue.shopshare.services.shopper.ShopperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ShopperGroupService implements IShopperGroupService {

    private final ShopperGroupRepository shopperGroupRepository;
    private final IShopperService shopperService;

    @Autowired
    public ShopperGroupService(ShopperGroupRepository shopperGroupRepository, ShopperService shopperService) {
        this.shopperGroupRepository = shopperGroupRepository;
        this.shopperService = shopperService;
    }

    @Override
    @Transactional
    public ShopperGroup create(String shopperGroupName, Shopper creator) {
        ShopperGroup shopperGroup = new ShopperGroup(shopperGroupName, creator);
        return create(shopperGroup);
    }

    @Override
    @Transactional
    public ShopperGroup create(ShopperGroup shopperGroup) {
        return shopperGroupRepository.save(shopperGroup);
    }

    @Override
    public ShopperGroup readById(UUID groupId) throws GroupNotFoundException {
        Optional<ShopperGroup> shopperGroup = shopperGroupRepository.findById(groupId);
        if (shopperGroup.isEmpty()) throw new GroupNotFoundException("Shopper group does not exist - " + groupId);

        return shopperGroup.get();
    }

    @Override
    public List<ShopperGroup> readByName(String name) {
        return shopperGroupRepository.findAllByName(name);
    }

    @Override
    public List<ShopperGroup> readByShopperId(UUID shopperId) {
        return shopperGroupRepository.findAllByCreatedBy_Id(shopperId);
    }

    @Override
    public List<ShopperGroup> read() {
        return shopperGroupRepository.findAll();
    }

    @Override
    @Transactional
    public boolean addShopperToGroup(UUID groupId, UUID shopperId) throws GroupNotFoundException {
        Optional<ShopperGroup> group = shopperGroupRepository.findById(groupId);
        if (group.isEmpty()) throw new GroupNotFoundException("Shopper group does not exist - " + groupId);
        Shopper shopper = shopperService.readById(shopperId);
        return group.get().addShopper(shopper);
    }
    @Override
    @Transactional
    public boolean addShopperToGroup(UUID groupId, Shopper shopper) throws GroupNotFoundException {
        Optional<ShopperGroup> group = shopperGroupRepository.findById(groupId);
        if (group.isEmpty()) throw new GroupNotFoundException("Shopper group does not exist - " + groupId);
        return group.get().addShopper(shopper);
    }

    @Override
    @Transactional
    public boolean removeShopperFromGroup(UUID groupId, UUID shopperId) throws GroupNotFoundException {
        Optional<ShopperGroup> group = shopperGroupRepository.findById(groupId);
        if (group.isEmpty()) throw new GroupNotFoundException("Shopper group does not exist - " + groupId);
        return group.get().removeShopper(shopperId);
    }
    @Override
    @Transactional
    public boolean removeShopperFromGroup(UUID groupId, Shopper shopper) throws GroupNotFoundException {
        Optional<ShopperGroup> group = shopperGroupRepository.findById(groupId);
        if (group.isEmpty()) throw new GroupNotFoundException("Shopper group does not exist - " + groupId);
        return group.get().removeShopper(shopper);
    }
}
