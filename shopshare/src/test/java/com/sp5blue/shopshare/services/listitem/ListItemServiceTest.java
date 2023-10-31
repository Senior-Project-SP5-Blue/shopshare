package com.sp5blue.shopshare.services.listitem;

import com.sp5blue.shopshare.dtos.listitem.CreateListItemRequest;
import com.sp5blue.shopshare.exceptions.shoppinglist.ListItemNotFoundException;
import com.sp5blue.shopshare.models.listitem.ItemStatus;
import com.sp5blue.shopshare.models.listitem.ListItem;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.repositories.ListItemRepository;
import com.sp5blue.shopshare.services.shoppergroup.IShopperGroupService;
import com.sp5blue.shopshare.services.shoppinglist.IShoppingListService;
import com.sp5blue.shopshare.services.user.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListItemServiceTest {

    @Mock
    ListItemRepository listItemRepository;

    @Mock
    IShopperGroupService shopperGroupService;

    @Mock
    IShoppingListService shoppingListService;

    @Mock
    IUserService userService;

    @InjectMocks
    ListItemService listItemService;

    @Test
    void addListItemToList_AddsItemToList() throws ExecutionException, InterruptedException {
        User user = new User();
        ShopperGroup shopperGroup = new ShopperGroup("Group 1", user);
        ShoppingList shoppingList = spy(new ShoppingList("Group 1's List", shopperGroup));
        CreateListItemRequest createListItemRequest = new CreateListItemRequest("List item one", false);
        when(userService.getUserById(user.getId())).thenReturn(CompletableFuture.completedFuture(user));
        when(shoppingListService.readShoppingListById(user.getId(), shopperGroup.getId(), shoppingList.getId())).thenReturn(CompletableFuture.completedFuture(shoppingList));
        when(listItemRepository.save(any(ListItem.class))).thenAnswer(l -> l.getArguments()[0]);

        var _result = listItemService.createListItem(user.getId(), shopperGroup.getId(), shoppingList.getId(), createListItemRequest);
        var result = _result.get();

        verify(shoppingList).setModifiedOn(any(LocalDateTime.class));
        verify(shoppingList).setModifiedBy(user);
        assertInstanceOf(ListItem.class, result);
        assertAll(
                () -> assertEquals("List item one", result.getName()),
                () -> assertEquals(user, result.getCreatedBy()));
    }

    @Test
    void removeListItemFromList_InvalidId_ThrowsListItemNotFoundException() {
        User user = new User();
        ShopperGroup shopperGroup = new ShopperGroup("Group 1", user);
        ShoppingList shoppingList = spy(new ShoppingList("Group 1's List", shopperGroup));
        UUID itemId = UUID.randomUUID();
        when(userService.getUserById(user.getId())).thenReturn(CompletableFuture.completedFuture(user));
        when(shoppingListService.readShoppingListById(user.getId(), shopperGroup.getId(), shoppingList.getId())).thenReturn(CompletableFuture.completedFuture(shoppingList));

        var exception = assertThrows(ListItemNotFoundException.class, () -> listItemService.removeListItemFromList(user.getId(), shopperGroup.getId(), shoppingList.getId(), itemId));
        assertEquals("List item does not exist - " + itemId, exception.getMessage());
    }

    @Test
    void removeListItemFromList() {
        User user = new User();
        ShopperGroup shopperGroup = new ShopperGroup("Group 1", user);
        ShoppingList shoppingList = spy(new ShoppingList("Group 1's List", shopperGroup));
        ArgumentCaptor<ListItem> removedListItem = ArgumentCaptor.forClass(ListItem.class);
        ListItem listItem1 = new ListItem("Item 1", user);
        ListItem listItem2 = new ListItem("Item 2", user);
        shoppingList.addItem(listItem1);
        shoppingList.addItem(listItem2);
        when(userService.getUserById(user.getId())).thenReturn(CompletableFuture.completedFuture(user));
        when(shoppingListService.readShoppingListById(user.getId(), shopperGroup.getId(), shoppingList.getId())).thenReturn(CompletableFuture.completedFuture(shoppingList));
        when(listItemRepository.findByList_IdAndId(shoppingList.getId(), listItem1.getId())).thenReturn(Optional.of(listItem1));

        listItemService.removeListItemFromList(user.getId(), shopperGroup.getId(), shoppingList.getId(), listItem1.getId());
        verify(shoppingList).setModifiedOn(any(LocalDateTime.class));
        verify(shoppingList).setModifiedBy(user);
        verify(listItemRepository).delete(removedListItem.capture());
        assertEquals(listItem1, removedListItem.getValue());
    }

    @Test
    void lockListItem_InvalidId_ThrowsListItemNotFoundException() {
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        UUID listId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        var exception = assertThrows(ListItemNotFoundException.class, () -> listItemService.lockListItem(userId, groupId, listId, itemId));
        assertEquals("List item does not exist - " + itemId, exception.getMessage());
    }
    @Test
    void lockListItem_LocksListItem() {
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        UUID listId = UUID.randomUUID();
        ListItem listItem = spy(new ListItem("Item 1"));
        listItem.setLocked(false);

        when(listItemRepository.findByList_IdAndId(listId, listItem.getId())).thenReturn(Optional.of(listItem));

        listItemService.lockListItem(userId, groupId, listId, listItem.getId());
        verify(listItem).setLocked(true);
        assertTrue(listItem.isLocked());
    }

    @Test
    void unlockListItem_InvalidId_ThrowsListItemNotFoundException() {
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        UUID listId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        var exception = assertThrows(ListItemNotFoundException.class, () -> listItemService.unlockListItem(userId, groupId, listId, itemId));
        assertEquals("List item does not exist - " + itemId, exception.getMessage());
    }
    @Test
    void unlockListItem_UnlocksListItem() {
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        UUID listId = UUID.randomUUID();
        ListItem listItem = spy(new ListItem("Item 1"));
        listItem.setLocked(true);

        when(listItemRepository.findByList_IdAndId(listId, listItem.getId())).thenReturn(Optional.of(listItem));

        listItemService.unlockListItem(userId, groupId, listId, listItem.getId());
        verify(listItem).setLocked(false);
        assertFalse(listItem.isLocked());
    }

    @Test
    void markListItemAsComplete_InvalidId_ThrowsListItemNoFoundException() {
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        UUID listId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        var exception = assertThrows(ListItemNotFoundException.class, () -> listItemService.markListItemAsComplete(userId, groupId, listId, itemId));
        assertEquals("List item does not exist - " + itemId, exception.getMessage());
    }

    @ParameterizedTest
    @EnumSource(names = {"ACTIVE", "ARCHIVED"})
    void markListItemAsComplete_SetsStatusToComplete(ItemStatus status) {
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        UUID listId = UUID.randomUUID();
        ListItem listItem = spy(new ListItem("Item 1"));
        listItem.setStatus(status);

        when(listItemRepository.findByList_IdAndId(listId, listItem.getId())).thenReturn(Optional.of(listItem));

        listItemService.markListItemAsComplete(userId, groupId, listId, listItem.getId());
        verify(listItem).setStatus(ItemStatus.COMPLETED);
        assertEquals(ItemStatus.COMPLETED, listItem.getStatus());
    }

    @Test
    void markListItemAsActive_InvalidId_ThrowsListItemNotFoundException() {
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        UUID listId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        var exception = assertThrows(ListItemNotFoundException.class, () -> listItemService.markListItemAsActive(userId, groupId, listId, itemId));
        assertEquals("List item does not exist - " + itemId, exception.getMessage());
    }

    @ParameterizedTest
    @EnumSource(names = {"ARCHIVED", "COMPLETED"})
    void markListItemAsActive_SetsStatusToActive(ItemStatus status) {
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        UUID listId = UUID.randomUUID();
        ListItem listItem = spy(new ListItem("Item 1"));
        listItem.setStatus(status);

        when(listItemRepository.findByList_IdAndId(listId, listItem.getId())).thenReturn(Optional.of(listItem));

        listItemService.markListItemAsActive(userId, groupId, listId, listItem.getId());
        verify(listItem).setStatus(ItemStatus.ACTIVE);
        assertEquals(ItemStatus.ACTIVE, listItem.getStatus());
    }
    @Test
    void markListItemAsArchived_InvalidId_ThrowsListItemNotFoundException() {
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        UUID listId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        var exception = assertThrows(ListItemNotFoundException.class, () -> listItemService.markListItemAsArchived(userId, groupId, listId, itemId));
        assertEquals("List item does not exist - " + itemId, exception.getMessage());
    }

    @ParameterizedTest
    @EnumSource(names = {"ACTIVE", "COMPLETED"})
    void markListItemAsArchived_SetsStatusToArchived(ItemStatus status) {
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        UUID listId = UUID.randomUUID();
        ListItem listItem = spy(new ListItem("Item 1"));
        listItem.setStatus(status);

        when(listItemRepository.findByList_IdAndId(listId, listItem.getId())).thenReturn(Optional.of(listItem));

        listItemService.markListItemAsArchived(userId, groupId, listId, listItem.getId());
        verify(listItem).setStatus(ItemStatus.ARCHIVED);
        assertEquals(ItemStatus.ARCHIVED, listItem.getStatus());
    }
    @Test
    void getListItemById_InvalidId_ThrowsListItemNotFoundException() {
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        UUID listId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        var exception = assertThrows(ListItemNotFoundException.class, () -> listItemService.getListItemById(userId, groupId, listId, itemId));
        assertEquals("List item does not exist - " + itemId, exception.getMessage());
    }

    @Test
    void getListItemById_ReturnsListItem() throws ExecutionException, InterruptedException {
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        UUID listId = UUID.randomUUID();
        ListItem listItem = new ListItem("Item 1");

        when(listItemRepository.findByList_IdAndId(listId, listItem.getId())).thenReturn(Optional.of(listItem));

        var _result = listItemService.readListItemById(userId, groupId, listId, listItem.getId());
        var result = _result.get();

        assertEquals(listItem, result);
    }

    @Test
    void getListItemsByCreator_NoMatches_ReturnsEmptyList() throws Exception {
        User user = new User();

        var results = listItemService.getListItemsByCreator(user.getId());
        assertTrue(results.get().isEmpty());
    }

    @Test
    void getListItemsByCreator_Matches_ReturnsListItems() throws Exception {
        User user = new User();
        ListItem listItem1 = new ListItem("Item 1", user);
        ListItem listItem2 = new ListItem("Item 2", user);

        when(listItemRepository.findAllByCreatedBy_Id(user.getId())).thenReturn(List.of(listItem1, listItem2));

        var results = listItemService.readListItemsByCreator(user.getId());
        assertEquals(2, results.get().size());
        assertAll(
                () -> assertEquals(listItem1, results.get().get(0)),
                () -> assertEquals(listItem2, results.get().get(1))
        );
    }
}