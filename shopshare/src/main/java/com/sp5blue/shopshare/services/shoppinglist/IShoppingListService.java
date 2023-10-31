package com.sp5blue.shopshare.services.shoppinglist;

import com.sp5blue.shopshare.dtos.shoppinglist.ShoppingListDto;
import com.sp5blue.shopshare.dtos.shoppinglist.SlimShoppingListDto;
import com.sp5blue.shopshare.exceptions.shoppinglist.ListNotFoundException;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IShoppingListService {

    CompletableFuture<SlimShoppingListDto> addShoppingList(UUID userId, UUID groupId, String name);

    CompletableFuture<ShoppingList> createShoppingList(UUID userId, UUID groupId, String name);

    CompletableFuture<SlimShoppingListDto> changeShoppingListName(UUID userId, UUID groupId, UUID listId, String newName);

    CompletableFuture<ShoppingList> updateShoppingListName(UUID userId, UUID groupId, UUID listId, String newName);

    CompletableFuture<ShoppingListDto> getShoppingListById(UUID userId, UUID groupId, UUID listId) throws ListNotFoundException;

    CompletableFuture<List<SlimShoppingListDto>> getShoppingLists(UUID userId, UUID groupId);

    CompletableFuture<ShoppingList> readShoppingListById(UUID userId, UUID groupId, UUID listId) throws ListNotFoundException;

    CompletableFuture<List<ShoppingList>> readShoppingLists(UUID userId, UUID groupId);

    CompletableFuture<Boolean> shoppingListExistsById(UUID listId);

    void verifyGroupHasList(UUID groupId, UUID listId);

    void deleteShoppingList(UUID userId, UUID groupId, UUID listId);
}
