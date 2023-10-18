package com.sp5blue.shopshare.services.shoppergroup;

import com.sp5blue.shopshare.exceptions.shoppergroup.GroupNotFoundException;
import com.sp5blue.shopshare.exceptions.shoppergroup.InvalidUserPermissionsException;
import com.sp5blue.shopshare.models.shopper.Shopper;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface IShopperGroupService {
    boolean removeShopperFromShopperGroup(UUID groupId, Shopper shopper) throws GroupNotFoundException;

    ShopperGroup createShopperGroup(Shopper admin, String shopperGroupName);
    ShopperGroup createShopperGroup(ShopperGroup shopperGroup);

    void deleteShopperGroup(UUID shopperId, UUID groupId) throws GroupNotFoundException, InvalidUserPermissionsException;

    void changeShopperGroupName(UUID groupId, String newName);

    boolean addShopperToShopperGroup(UUID groupId, UUID shopperId) throws GroupNotFoundException;

    boolean addShopperToShopperGroup(UUID groupId, Shopper shopper) throws GroupNotFoundException;

    boolean removeShopperFromShopperGroup(UUID groupId, UUID shopperId) throws GroupNotFoundException;

    ShopperGroup createShopperGroup(UUID creatorId, String groupName);

    List<ShopperGroup> getShopperGroupsByShopper(UUID shopperId);

    List<ShopperGroup> getShopperGroupsByShopper(Shopper shopper);

    ShopperGroup getShopperGroupById(UUID groupId) throws GroupNotFoundException;

    ShopperGroup getShopperGroupByShopperAndId(UUID shopperId, UUID groupId) throws GroupNotFoundException;

    ShopperGroup getShopperGroupByShopperAndId(Shopper shopper, UUID groupId) throws GroupNotFoundException;

    List<ShopperGroup> getShopperGroupsByName(String name);

    List<ShopperGroup> getShopperGroupsByShopperAndName(UUID shopperId, String name);

    List<ShopperGroup> getShopperGroupsByShopperAndName(Shopper shopper, String name);

    List<ShopperGroup> getShopperGroupsByAdmin(UUID shopperId);

    List<ShopperGroup> getShopperGroups();

    boolean shopperGroupExistsById(UUID groupId);
}
