package com.sp5blue.shopshare.services.listitem;

import com.sp5blue.shopshare.dtos.listitem.CreateListItemRequest;
import com.sp5blue.shopshare.dtos.listitem.EditListItemRequest;
import com.sp5blue.shopshare.dtos.listitem.ListItemDto;
import com.sp5blue.shopshare.exceptions.shoppinglist.ListItemNotFoundException;
import com.sp5blue.shopshare.exceptions.shoppinglist.ListNotFoundException;
import com.sp5blue.shopshare.models.listitem.ListItem;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IListItemService {

    CompletableFuture<ListItemDto> addListItemToList(UUID userId, UUID groupId, UUID listId, CreateListItemRequest listItemDto);

    @Transactional
    @Async
    CompletableFuture<ListItem> createListItem(UUID userId, UUID groupId, UUID listId, CreateListItemRequest createListItemRequest);

    void removeListItemFromList(UUID userId, UUID groupId, UUID listId, UUID itemId);

    void lockListItem(UUID userId, UUID groupId, UUID listId, UUID itemId);

    void unlockListItem(UUID userId, UUID groupId, UUID listId, UUID itemId);

    void markListItemAsComplete(UUID userId, UUID groupId, UUID listId, UUID itemId);

    void markListItemAsActive(UUID userId, UUID groupId, UUID listId, UUID itemId);

    void markListItemAsArchived(UUID userId, UUID groupId, UUID listId, UUID itemId);

    CompletableFuture<ListItemDto> getListItemById(UUID userId, UUID groupId, UUID listId, UUID itemId) throws ListNotFoundException;

    @Async
    CompletableFuture<ListItem> readListItemById(UUID userId, UUID groupId, UUID listId, UUID itemId) throws ListItemNotFoundException;

    CompletableFuture<List<ListItemDto>> getListItemsByCreator(UUID userId);

    void removeListItemsFromList(UUID userId, UUID groupId, UUID listId);

    @Async
    CompletableFuture<List<ListItem>> readListItemsByCreator(UUID userId);

    CompletableFuture<ListItem> editListItem(UUID userId, UUID groupId, UUID listId, UUID itemId, EditListItemRequest editListItemRequest) throws ListItemNotFoundException;
}
