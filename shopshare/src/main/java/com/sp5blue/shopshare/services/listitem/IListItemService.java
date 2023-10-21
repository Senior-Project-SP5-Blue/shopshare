package com.sp5blue.shopshare.services.listitem;

import com.sp5blue.shopshare.exceptions.shoppinglist.ListItemNotFoundException;
import com.sp5blue.shopshare.exceptions.shoppinglist.ListNotFoundException;
import com.sp5blue.shopshare.models.listitem.EditListItemDto;
import com.sp5blue.shopshare.models.listitem.ListItem;
import com.sp5blue.shopshare.models.listitem.CreateListItemDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IListItemService {

    CompletableFuture<ListItem> addListItemToList(UUID userId, UUID groupId, UUID listId, CreateListItemDto listItemDto);

    void removeListItemFromList(UUID userId, UUID groupId, UUID listId, UUID itemId);

    void lockListItem(UUID userId, UUID groupId, UUID listId, UUID itemId);

    void unlockListItem(UUID userId, UUID groupId, UUID listId, UUID itemId);

    void markListItemAsComplete(UUID userId, UUID groupId, UUID listId, UUID itemId);

    void markListItemAsActive(UUID userId, UUID groupId, UUID listId, UUID itemId);

    void markListItemAsArchived(UUID userId, UUID groupId, UUID listId, UUID itemId);

    CompletableFuture<ListItem> getListItemById(UUID userId, UUID groupId, UUID listId, UUID itemId) throws ListNotFoundException;

    CompletableFuture<List<ListItem>> getListItemsByCreator(UUID userId);

    CompletableFuture<List<ListItem>> getListItemsByShoppingList(UUID userId, UUID groupId, UUID listId);

    void removeListItemsFromList(UUID userId, UUID groupId, UUID listId);

    void editListItem(UUID userId, UUID groupId, UUID listId, UUID itemId, EditListItemDto editListItemDto) throws ListItemNotFoundException;
}
