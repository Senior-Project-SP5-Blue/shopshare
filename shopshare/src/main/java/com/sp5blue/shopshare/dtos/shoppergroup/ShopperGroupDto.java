package com.sp5blue.shopshare.dtos.shoppergroup;

import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import com.sp5blue.shopshare.models.user.User;
import java.util.List;
import java.util.UUID;

public record ShopperGroupDto(
    UUID id, String name, String admin, List<String> users, List<slimList> lists) {
  private record slimList(UUID id, String name) {
    public slimList(ShoppingList list) {
      this(list.getId(), list.getName());
    }
  }

  public ShopperGroupDto(ShopperGroup shopperGroup) {
    this(
        shopperGroup.getId(),
        shopperGroup.getName(),
        shopperGroup.getAdmin().getUsername(),
        shopperGroup.getUsers().stream().map(User::getUsername).toList(),
        shopperGroup.getLists().stream().map(slimList::new).toList());
  }
}
