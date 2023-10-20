package com.sp5blue.shopshare.services.listitem;

import com.sp5blue.shopshare.exceptions.shoppinglist.ListItemNotFoundException;
import com.sp5blue.shopshare.models.listitem.ItemStatus;
import com.sp5blue.shopshare.models.listitem.ListItem;
import com.sp5blue.shopshare.models.listitem.CreateListItemDto;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import com.sp5blue.shopshare.repositories.ListItemRepository;
import com.sp5blue.shopshare.services.user.IUserService;
import com.sp5blue.shopshare.services.shoppergroup.IShopperGroupService;
import com.sp5blue.shopshare.services.shoppinglist.IShoppingListService;
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
    void addListItemToList_AddsItemToList() {
        User user = new User();
        ShopperGroup shopperGroup = new ShopperGroup("Group 1", user);
        ShoppingList shoppingList = spy(new ShoppingList("Group 1's List", shopperGroup));
        CreateListItemDto createListItemDto = new CreateListItemDto("List item one", false);
        when(userService.getUserById(user.getId())).thenReturn(user);
        when(shoppingListService.getShoppingListById(user.getId(), shopperGroup.getId(), shoppingList.getId())).thenReturn(shoppingList);
        when(listItemRepository.save(any(ListItem.class))).thenAnswer(l -> l.getArguments()[0]);

        var result = listItemService.addListItemToList(user.getId(), shopperGroup.getId(), shoppingList.getId(), createListItemDto);
        verify(shoppingList).setModifiedOn(any(LocalDateTime.class));
        verify(shoppingList).setModifiedBy(user);
        assertInstanceOf(ListItem.class, result);
        assertAll(
                () -> assertEquals("List item one", result.getName()),
                () -> assertEquals(user, result.getCreatedBy()),
                () -> assertEquals(shoppingList, result.getList())
        );
    }

    @Test
    void removeListItemFromList_InvalidId_ThrowsListItemNotFoundException() {
        User user = new User();
        ShopperGroup shopperGroup = new ShopperGroup("Group 1", user);
        ShoppingList shoppingList = spy(new ShoppingList("Group 1's List", shopperGroup));
        UUID itemId = UUID.randomUUID();

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
        when(userService.getUserById(user.getId())).thenReturn(user);
        when(shoppingListService.getShoppingListById(user.getId(), shopperGroup.getId(), shoppingList.getId())).thenReturn(shoppingList);
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
    void getListItemById_ReturnsListItem() {
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        UUID listId = UUID.randomUUID();
        ListItem listItem = new ListItem("Item 1");

        when(listItemRepository.findByList_IdAndId(listId, listItem.getId())).thenReturn(Optional.of(listItem));

        var result = listItemService.getListItemById(userId, groupId, listId, listItem.getId());
        assertEquals(listItem, result);
    }

    @Test
    void getListItemsByCreator_NoMatches_ReturnsEmptyList() {
        User user = new User();

        var results = listItemService.getListItemsByCreator(user.getId());
        assertTrue(results.isEmpty());
    }

    @Test
    void getListItemsByCreator_Matches_ReturnsListItems() {
        User user = new User();
        ListItem listItem1 = new ListItem("Item 1", user);
        ListItem listItem2 = new ListItem("Item 2", user);

        when(listItemRepository.findAllByCreatedBy_Id(user.getId())).thenReturn(List.of(listItem1, listItem2));

        var results = listItemService.getListItemsByCreator(user.getId());
        assertEquals(2, results.size());
        assertAll(
                () -> assertEquals(listItem1, results.get(0)),
                () -> assertEquals(listItem2, results.get(1))
        );
    }

    @Test
    void getListItemsByShoppingList_NoMatches_ReturnsEmptyList() {
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        UUID listId = UUID.randomUUID();

        var results = listItemService.getListItemsByShoppingList(userId, groupId, listId);
        assertTrue(results.isEmpty());
    }

    @Test
    void getListItemsByShoppingList_Matches_ReturnsListItems() {
        ShopperGroup shopperGroup = new ShopperGroup();
        ShoppingList shoppingList = new ShoppingList();
        User user = new User();
        ListItem listItem1 = new ListItem("Item 1", user);
        ListItem listItem2 = new ListItem("Item 2", user);
        shoppingList.addItem(listItem1);
        shoppingList.addItem(listItem2);

        when(listItemRepository.findAllByList_Id(shoppingList.getId())).thenReturn(shoppingList.getItems());

        var results = listItemService.getListItemsByShoppingList(user.getId(), shopperGroup.getId(), shoppingList.getId());
        assertEquals(2, results.size());
        assertAll(
                () -> assertEquals(listItem1, results.get(0)),
                () -> assertEquals(listItem2, results.get(1))
        );
    }
}