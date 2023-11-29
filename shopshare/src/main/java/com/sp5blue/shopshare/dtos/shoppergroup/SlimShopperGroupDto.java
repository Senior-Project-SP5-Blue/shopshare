package com.sp5blue.shopshare.dtos.shoppergroup;

import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import java.util.UUID;

public record SlimShopperGroupDto(
    UUID id, String name, String admin, Integer userCount, Integer listCount, String color) {
  public SlimShopperGroupDto(ShopperGroup shopperGroup) {
    this(
        shopperGroup.getId(),
        shopperGroup.getName(),
        shopperGroup.getAdmin().getUsername(),
        shopperGroup.getUsers().size(),
        shopperGroup.getLists().size(),
        shopperGroup.getColor());
  }
}
