package com.sp5blue.shopshare.services.listitem;

import com.sp5blue.shopshare.exceptions.shoppinglist.ListNotFoundException;
import com.sp5blue.shopshare.models.listitem.ListItem;
import com.sp5blue.shopshare.models.listitem.ListItemDto;

import java.util.List;
import java.util.UUID;

public interface IListItemService {

    ListItem addListItemToList(UUID userId, UUID groupId, UUID listId, ListItemDto listItemDto);

    void removeListItemFromList(UUID userId, UUID groupId, UUID listId, UUID itemId);

    void lockListItem(UUID userId, UUID groupId, UUID listId, UUID itemId);

    void unlockListItem(UUID userId, UUID groupId, UUID listId, UUID itemId);

    void markListItemAsComplete(UUID userId, UUID groupId, UUID listId, UUID itemId);

    void markListItemAsActive(UUID userId, UUID groupId, UUID listId, UUID itemId);

    void markListItemAsArchived(UUID userId, UUID groupId, UUID listId, UUID itemId);

    ListItem getListItemById(UUID userId, UUID groupId, UUID listId, UUID itemId) throws ListNotFoundException;

    List<ListItem> getListItemsByCreator(UUID userId);

    List<ListItem> getListItemsByShoppingList(UUID userId, UUID groupId, UUID listId);
}
