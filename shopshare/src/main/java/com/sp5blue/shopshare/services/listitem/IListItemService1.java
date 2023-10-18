package com.sp5blue.shopshare.services.listitem;

import com.sp5blue.shopshare.exceptions.shoppinglist.ListNotFoundException;
import com.sp5blue.shopshare.models.listitem.ListItem;
import com.sp5blue.shopshare.models.listitem.ListItemDto;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;

import java.util.List;
import java.util.UUID;

public interface IListItemService1 {
    ListItem createListItem(String name);

    ListItem createListItem(ListItem listItem);

    ListItem getListItemById(UUID id) throws ListNotFoundException;

    List<ListItem> getListItemsByShopper(UUID shopperId);

    List<ListItem> getListItemsByShoppingList(UUID listId);

    boolean addListItemToShoppingList(ShoppingList list, ListItemDto listItemDto);
}
