package com.sp5blue.shopshare.services.shoppergroup;

import com.sp5blue.shopshare.exceptions.shoppergroup.GroupNotFoundException;
import com.sp5blue.shopshare.models.Shopper;
import com.sp5blue.shopshare.models.ShopperGroup;

import java.util.List;
import java.util.UUID;

public interface IShopperGroupService {
    boolean removeShopperFromGroup(UUID groupId, Shopper shopper) throws GroupNotFoundException;

    ShopperGroup create(String shopperGroupName, Shopper creator);
    ShopperGroup create(ShopperGroup shopperGroup);
    boolean addShopperToGroup(UUID groupId, UUID shopperId) throws GroupNotFoundException;

    boolean addShopperToGroup(UUID groupId, Shopper shopper) throws GroupNotFoundException;

    boolean removeShopperFromGroup(UUID groupId, UUID shopperId) throws GroupNotFoundException;

    ShopperGroup readById(UUID groupId) throws GroupNotFoundException;

    List<ShopperGroup> readByName(String name);

    List<ShopperGroup> readByShopperId(UUID shopperId);
    List<ShopperGroup> read();
}
