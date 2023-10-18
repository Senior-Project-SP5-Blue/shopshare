package com.sp5blue.shopshare.services.shoppinglist;

import com.sp5blue.shopshare.exceptions.shoppinglist.ListNotFoundException;
import com.sp5blue.shopshare.models.listitem.ListItem;
import com.sp5blue.shopshare.models.listitem.ListItemDto;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface IShoppingListService {
    ShoppingList createShoppingList(ShopperGroup group, String name);

    ShoppingList createShoppingList(UUID groupId, String name);

    ShoppingList createShoppingList(ShoppingList shoppingList);

    void changeShoppingListName(UUID shoppingListId, String newName);

    @Transactional
    void changeShoppingListName(UUID groupId, UUID shoppingListId, String newName);

    ShoppingList getShoppingListById(UUID id) throws ListNotFoundException;

    List<ShoppingList> getShoppingListsByName(String name) throws ListNotFoundException;

    List<ShoppingList> getShoppingListsByShopperGroupId(UUID shopperGroupId);

    boolean removeItemFromShoppingList(UUID listId, UUID itemId) throws ListNotFoundException;

    boolean removeItemFromShoppingList(UUID listId, ListItem item) throws ListNotFoundException;

//    boolean addItemToShoppingList(UUID listId, UUID itemId) throws ListNotFoundException;

    boolean removeItemToShoppingList(UUID groupId, UUID listId, UUID itemId) throws ListNotFoundException;

    boolean addItemToShoppingList(UUID listId, ListItem item) throws ListNotFoundException;

    boolean addItemToShoppingList(UUID userId, UUID groupId, UUID listId, ListItemDto listItemDto) throws ListNotFoundException;

    boolean addItemToShoppingList(UUID creatorId, UUID listId, String name) throws ListNotFoundException;

    boolean shoppingListExistsById(UUID listId);
}
