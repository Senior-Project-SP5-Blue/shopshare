package com.sp5blue.shopshare.dtos.shoppinglist;

import com.sp5blue.shopshare.dtos.listitem.ListItemDto;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ShoppingListDto(
    UUID id, String name, LocalDateTime modifiedOn, List<ListItemDto> items) {
  public ShoppingListDto(ShoppingList shoppingList) {
    this(
        shoppingList.getId(),
        shoppingList.getName(),
        shoppingList.getModifiedOn(),
        shoppingList.getItems().stream().map(ListItemDto::new).toList());
  }
}
