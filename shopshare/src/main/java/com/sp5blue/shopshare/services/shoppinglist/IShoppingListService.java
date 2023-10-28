package com.sp5blue.shopshare.services.shoppinglist;

import com.sp5blue.shopshare.exceptions.shoppinglist.ListNotFoundException;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingListDto;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IShoppingListService {

    CompletableFuture<ShoppingListDto> createShoppingList(UUID userId, UUID groupId, String name);

    CompletableFuture<ShoppingListDto> changeShoppingListName(UUID userId, UUID groupId, UUID listId, String newName);

    CompletableFuture<ShoppingList> getShoppingListById(UUID userId, UUID groupId, UUID listId) throws ListNotFoundException;

    CompletableFuture<List<ShoppingListDto>> getShoppingLists(UUID userId, UUID groupId);

    CompletableFuture<Boolean> shoppingListExistsById(UUID listId);

    void verifyGroupHasList(UUID groupId, UUID listId);

    void deleteShoppingList(UUID userId, UUID groupId, UUID listId);
}
