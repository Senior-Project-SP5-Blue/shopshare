package com.sp5blue.shopshare.services.shoppinglist;

import com.sp5blue.shopshare.exceptions.shoppinglist.ListNotFoundException;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingListDto;

import java.util.List;
import java.util.UUID;

public interface IShoppingListService {

    ShoppingListDto createShoppingList(UUID userId, UUID groupId, String name);

    void changeShoppingListName(UUID userId, UUID groupId, UUID listId, String newName);

    ShoppingList getShoppingListById(UUID userId, UUID groupId, UUID listId) throws ListNotFoundException;

    List<ShoppingListDto> getShoppingLists(UUID userId, UUID groupId);

    boolean shoppingListExistsById(UUID listId);

    void verifyGroupHasList(UUID groupId, UUID listId);

    void deleteShoppingList(UUID userId, UUID groupId, UUID listId);
}
