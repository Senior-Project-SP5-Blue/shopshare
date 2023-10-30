package com.sp5blue.shopshare.services.listitem;

import com.sp5blue.shopshare.exceptions.shoppinglist.ListItemNotFoundException;
import com.sp5blue.shopshare.exceptions.shoppinglist.ListNotFoundException;
import com.sp5blue.shopshare.models.listitem.CreateListItemRequest;
import com.sp5blue.shopshare.models.listitem.EditListItemRequest;
import com.sp5blue.shopshare.models.listitem.ListItem;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IListItemService {

    CompletableFuture<ListItem> addListItemToList(UUID userId, UUID groupId, UUID listId, CreateListItemRequest listItemDto);

    void removeListItemFromList(UUID userId, UUID groupId, UUID listId, UUID itemId);

    void lockListItem(UUID userId, UUID groupId, UUID listId, UUID itemId);

    void unlockListItem(UUID userId, UUID groupId, UUID listId, UUID itemId);

    void markListItemAsComplete(UUID userId, UUID groupId, UUID listId, UUID itemId);

    void markListItemAsActive(UUID userId, UUID groupId, UUID listId, UUID itemId);

    void markListItemAsArchived(UUID userId, UUID groupId, UUID listId, UUID itemId);

    CompletableFuture<ListItem> getListItemById(UUID userId, UUID groupId, UUID listId, UUID itemId) throws ListNotFoundException;

    CompletableFuture<List<ListItem>> getListItemsByCreator(UUID userId);

    void removeListItemsFromList(UUID userId, UUID groupId, UUID listId);

    CompletableFuture<ListItem> editListItem(UUID userId, UUID groupId, UUID listId, UUID itemId, EditListItemRequest editListItemRequest) throws ListItemNotFoundException;
}
