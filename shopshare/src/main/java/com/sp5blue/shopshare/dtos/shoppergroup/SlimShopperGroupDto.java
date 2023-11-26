package com.sp5blue.shopshare.dtos.shoppergroup;

import com.sp5blue.shopshare.dtos.shoppinglist.SlimShoppingListDto;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import com.sp5blue.shopshare.models.user.User;

import java.util.List;
import java.util.UUID;

public record SlimShopperGroupDto(
    UUID id, String name, String admin, Integer userCount, Integer listCount) {
  public SlimShopperGroupDto(ShopperGroup shopperGroup) {
    this(
        shopperGroup.getId(),
        shopperGroup.getName(),
        shopperGroup.getAdmin().getUsername(),
        shopperGroup.getUsers().size(),
        shopperGroup.getLists().size());
  }
}
