package com.sp5blue.shopshare.dtos.shoppinglist;

import com.sp5blue.shopshare.models.listitem.ItemStatus;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import java.time.LocalDateTime;
import java.util.UUID;

public record SlimShoppingListDto(
    UUID id, String name, LocalDateTime modifiedOn, Number completed, int total, UUID groupId) {

  public SlimShoppingListDto(ShoppingList shoppingList) {
    this(
        shoppingList.getId(),
        shoppingList.getName(),
        shoppingList.getModifiedOn(),
        shoppingList.getItems().stream().filter(i -> i.getStatus() == ItemStatus.COMPLETED).count(),
        shoppingList.getItems().size(),
        shoppingList.getGroup().getId());
  }
}
