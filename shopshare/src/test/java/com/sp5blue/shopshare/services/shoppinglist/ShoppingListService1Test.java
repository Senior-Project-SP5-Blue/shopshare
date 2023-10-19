package com.sp5blue.shopshare.services.shoppinglist;

import com.sp5blue.shopshare.exceptions.shoppinglist.ListNotFoundException;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import com.sp5blue.shopshare.repositories.Shopper;
import com.sp5blue.shopshare.repositories.ShoppingListRepository;
import com.sp5blue.shopshare.services.shoppergroup.IShopperGroupService1;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ShoppingListService1Test {

    @Mock
    ShoppingListRepository shoppingListRepository;

    @Mock
    IShopperGroupService1 shopperGroupService;

    @InjectMocks
    ShoppingListService1 shoppingListService;


    @Test
    void createShoppingList_CreatesShoppingList() {
        ShopperGroup shopperGroup = new ShopperGroup();
        ArgumentCaptor<ShoppingList> addedList = ArgumentCaptor.forClass(ShoppingList.class);
        UUID userId = UUID.randomUUID();
        when(shopperGroupService.getShopperGroupById(userId, shopperGroup.getId())).thenReturn(shopperGroup);
        when(shoppingListRepository.save(any(ShoppingList.class))).thenAnswer(m -> m.getArguments()[0]);

        var result = shoppingListService.createShoppingList(userId, shopperGroup.getId(), "New List");
        verify(shoppingListRepository).save(addedList.capture());
        assertEquals(addedList.getValue(), result);

    }

    @Test
    void changeShoppingListName_InvalidId_ThrowsListNotFoundException() {
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        UUID listId = UUID.randomUUID();

        var exception = assertThrows(ListNotFoundException.class, () -> shoppingListService.changeShoppingListName(userId, groupId, listId, "New Name"));
        assertEquals("Shopping list does not exist - " + listId, exception.getMessage());
    }

    @Test
    void changeShoppingListName_ChangesListName() {
        UUID userId = UUID.randomUUID();
        ShoppingList shoppingList = new ShoppingList();
        UUID groupId = UUID.randomUUID();
        when(shoppingListRepository.findByGroup_IdAndId(groupId, shoppingList.getId())).thenReturn(Optional.of(shoppingList));

        shoppingListService.changeShoppingListName(userId, groupId, shoppingList.getId(), "New Name");
        assertEquals("New Name", shoppingList.getName());
    }

    @Test
    void getShoppingListById_InvalidId_ThrowsListNotFoundException() {
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        UUID listId = UUID.randomUUID();

        var exception = assertThrows(ListNotFoundException.class, () -> shoppingListService.getShoppingListById(userId, groupId, listId));
        assertEquals("Shopping list does not exist - " + listId, exception.getMessage());
    }

    @Test
    void getShoppingListById_ReturnsShoppingList() {
        UUID userId = UUID.randomUUID();
        ShoppingList shoppingList = new ShoppingList();
        UUID groupId = UUID.randomUUID();
        when(shoppingListRepository.findByGroup_IdAndId(groupId, shoppingList.getId())).thenReturn(Optional.of(shoppingList));

        var result = shoppingListService.getShoppingListById(userId, groupId, shoppingList.getId());
        assertEquals(shoppingList, result);
    }

    @Test
    void getShoppingLists_NoMatches_ReturnsEmptyList() {
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();

        var results = shoppingListService.getShoppingLists(userId, groupId);
        assertTrue(results.isEmpty());
    }

    @Test
    void getShoppingLists_Matches_ReturnsShoppingLists() {
        UUID userId = UUID.randomUUID();
        ShoppingList shoppingList1 = new ShoppingList("List 1");
        ShoppingList shoppingList2 = new ShoppingList("List 2");
        ShopperGroup shopperGroup = new ShopperGroup();
        shopperGroup.addList(shoppingList1);
        shopperGroup.addList(shoppingList2);
        when(shoppingListRepository.findAllByGroup_Id(shopperGroup.getId())).thenReturn(shopperGroup.getLists());

        var results = shoppingListService.getShoppingLists(userId, shopperGroup.getId());
        assertEquals(2, results.size());
        assertAll (
                () -> assertEquals(shoppingList1, results.get(0)),
                () -> assertEquals(shoppingList2, results.get(1))
        );
    }
}