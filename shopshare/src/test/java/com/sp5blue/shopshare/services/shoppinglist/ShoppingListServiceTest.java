package com.sp5blue.shopshare.services.shoppinglist;

import com.sp5blue.shopshare.exceptions.shoppinglist.ListNotFoundException;
import com.sp5blue.shopshare.models.ListItem;
import com.sp5blue.shopshare.models.Shopper;
import com.sp5blue.shopshare.models.ShopperGroup;
import com.sp5blue.shopshare.models.ShoppingList;
import com.sp5blue.shopshare.repositories.ShoppingListRepository;
import com.sp5blue.shopshare.services.listitem.ListItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShoppingListServiceTest {

    @Mock
    ShoppingListRepository mockShoppingListRepo;

    @Mock
    ListItemService mockListItemService;

    @InjectMocks
    ShoppingListService shoppingListService;

    @Test
    void create_CreatesNewList_ReturnsNewList() {
        ShoppingList shoppingList = new ShoppingList();
        when(mockShoppingListRepo.save(shoppingList)).thenReturn(shoppingList);

        var result = shoppingListService.create(shoppingList);
        assertEquals(shoppingList, result);
    }

    @Test
    void readById_InvalidId_ThrowsListNotFoundException() {
        UUID shoppingListId = UUID.randomUUID();
        when(mockShoppingListRepo.findById(shoppingListId)).thenReturn(Optional.empty());

        var exception = assertThrows(ListNotFoundException.class, () -> shoppingListService.readById(shoppingListId));

        assertEquals("Shopping list does not exist - " + shoppingListId, exception.getMessage());
    }

    @Test
    void readById_ValidId_ThrowsListNotFoundException() {
        ShoppingList shoppingList = new ShoppingList();
        when(mockShoppingListRepo.findById(shoppingList.getId())).thenReturn(Optional.of(shoppingList));

        var result = shoppingListService.readById(shoppingList.getId());

        assertEquals(shoppingList, result);
    }

    @Test
    void readByName_NoMatches_ReturnsEmptyList() {
        var result = shoppingListService.readByName("List 1");

        assertEquals(0, result.size());
    }

    @Test
    void readByName_MultipleMatches_ReturnsMatches() {
        ShoppingList shoppingList1 = new ShoppingList();
        ShoppingList shoppingList2 = new ShoppingList();
        ShoppingList shoppingList3 = new ShoppingList();
        when(mockShoppingListRepo.findAllByName("List 1")).thenReturn(Arrays.asList(shoppingList1, shoppingList2, shoppingList3));

        var result = shoppingListService.readByName("List 1");

        assertEquals(3, result.size());
        assertAll(
                () -> assertEquals(shoppingList1, result.get(0)),
                () -> assertEquals(shoppingList2, result.get(1)),
                () -> assertEquals(shoppingList3, result.get(2))
        );
    }

    @Test
    void readByShopperId() {
        Shopper shopper = new Shopper();
        ShoppingList shoppingList1 = new ShoppingList("Shopping list 1", shopper);
        ShoppingList shoppingList2 = new ShoppingList("Shopping list 2", shopper);
        when(mockShoppingListRepo.findAllByShopper_Id(shopper.getId())).thenReturn(Arrays.asList(shoppingList1, shoppingList2));

        var results = shoppingListService.readByShopperId(shopper.getId());

        assertEquals(2, results.size());
        assertAll(
                () -> assertEquals(shoppingList1, results.get(0)),
                () -> assertEquals(shoppingList2, results.get(1))
        );
    }

    @Test
    void readByShopperGroupId() {
        ShopperGroup shopperGroup = new ShopperGroup();
        ShoppingList shoppingList1 = new ShoppingList("Shopping list 1", shopperGroup);
        ShoppingList shoppingList2 = new ShoppingList("Shopping list 2", shopperGroup);
        when(mockShoppingListRepo.findAllByShopper_Id(shopperGroup.getId())).thenReturn(Arrays.asList(shoppingList1, shoppingList2));

        var results = shoppingListService.readByShopperId(shopperGroup.getId());

        assertEquals(2, results.size());
        assertAll(
                () -> assertEquals(shoppingList1, results.get(0)),
                () -> assertEquals(shoppingList2, results.get(1))
        );
    }

    @Test
    void removeItemFromList_InvalidId_ThrowsListNotFoundException() {
        UUID shoppingListId = UUID.randomUUID();
        ListItem item = new ListItem("Item 1");

        var exception = assertThrows(ListNotFoundException.class, () -> shoppingListService.removeItemFromList(shoppingListId, item.getId()));
        assertEquals("Shopping list does not exist - " + shoppingListId, exception.getMessage());
    }

    @Test
    void removeItemFromList_ValidId_RemovesItemFromList() {
        ShopperGroup shopperGroup = new ShopperGroup();
        ShoppingList shoppingList1 = new ShoppingList("Shopping list 1", shopperGroup);
        ListItem item1 = new ListItem("Item 1");
        ListItem item2 = new ListItem("Item 2");
        shoppingList1.addItem(item1);
        shoppingList1.addItem(item2);
        when(mockShoppingListRepo.findById(shopperGroup.getId())).thenReturn(Optional.of(shoppingList1));

        var result = shoppingListService.removeItemFromList(shopperGroup.getId(), item1.getId());

        assertTrue(result);
        assertEquals(1, shoppingList1.getItems().size());
        assertEquals(item2, shoppingList1.getItems().get(0));
    }

    @Test
    void addItemToList_InvalidId_ThrowsListNotFoundException() {
        UUID shoppingListId = UUID.randomUUID();
        ListItem item = new ListItem("Item 1");

        var exception = assertThrows(ListNotFoundException.class, () -> shoppingListService.addItemToList(shoppingListId, item.getId()));
        assertEquals("Shopping list does not exist - " + shoppingListId, exception.getMessage());
    }

    @Test
    void addItemToList_ValidId_AddsItemToList() {
        ShopperGroup shopperGroup = new ShopperGroup();
        ShoppingList shoppingList1 = new ShoppingList("Shopping list 1", shopperGroup);
        ListItem item1 = new ListItem("Item 1");
        ListItem item2 = new ListItem("Item 2");
        shoppingList1.addItem(item1);
        when(mockShoppingListRepo.findById(shopperGroup.getId())).thenReturn(Optional.of(shoppingList1));

        var result = shoppingListService.addItemToList(shopperGroup.getId(), item2);

        assertTrue(result);
        assertEquals(2, shoppingList1.getItems().size());
        assertAll(
                () -> assertEquals(item1, shoppingList1.getItems().get(0)),
                () -> assertEquals(item2, shoppingList1.getItems().get(1))
        );
    }
}