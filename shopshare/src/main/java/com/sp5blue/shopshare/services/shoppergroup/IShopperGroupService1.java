package com.sp5blue.shopshare.services.shoppergroup;

import com.sp5blue.shopshare.exceptions.shoppergroup.GroupNotFoundException;
import com.sp5blue.shopshare.exceptions.shoppergroup.InvalidUserPermissionsException;
import com.sp5blue.shopshare.exceptions.shoppergroup.RemoveGroupAdminException;
import com.sp5blue.shopshare.models.shopper.Shopper;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface IShopperGroupService1 {
    ShopperGroup createShopperGroup(UUID adminId, String groupName);

    List<ShopperGroup> getShopperGroups(UUID userId);

    ShopperGroup getShopperGroupById(UUID userId, UUID groupId) throws GroupNotFoundException;

    void deleteShopperGroup(UUID userId, UUID groupId) throws GroupNotFoundException, InvalidUserPermissionsException;

    void changeShopperGroupName(UUID userId, UUID groupId, String newName);

    boolean addShopperToShopperGroup(UUID groupId, UUID shopperId) throws GroupNotFoundException;

    boolean addShopperToShopperGroup(UUID groupId, Shopper shopper) throws GroupNotFoundException;

    boolean removeShopperFromShopperGroup(UUID userId, UUID groupId, UUID shopperId) throws GroupNotFoundException, RemoveGroupAdminException;
}
