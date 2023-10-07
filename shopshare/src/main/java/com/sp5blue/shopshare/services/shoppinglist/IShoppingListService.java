package com.sp5blue.shopshare.services.shoppinglist;

import com.sp5blue.shopshare.exceptions.shoppinglist.ListNotFoundException;
import com.sp5blue.shopshare.models.ListItem;
import com.sp5blue.shopshare.models.Shopper;
import com.sp5blue.shopshare.models.ShopperGroup;
import com.sp5blue.shopshare.models.ShoppingList;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface IShoppingListService {
    ShoppingList create(String name, Shopper shopper);

    ShoppingList create(String name, ShopperGroup group);

    ShoppingList create(ShoppingList shoppingList);

    ShoppingList readById(UUID id) throws ListNotFoundException;

    List<ShoppingList> readByName(String name) throws ListNotFoundException;

    List<ShoppingList> readByShopperId(UUID shopperId);

    List<ShoppingList> readByShopperGroupId(UUID shopperGroupId);

    boolean removeItemFromList(UUID listId, UUID itemId) throws ListNotFoundException;

    boolean removeItemFromList(UUID listId, ListItem item) throws ListNotFoundException;

    @Transactional
    boolean addItemToList(UUID listId, UUID itemId) throws ListNotFoundException;

    boolean addItemToList(UUID listId, ListItem item) throws ListNotFoundException;
}
