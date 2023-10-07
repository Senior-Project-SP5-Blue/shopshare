package com.sp5blue.shopshare.services.listitem;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sp5blue.shopshare.exceptions.authentication.UserNotFoundException;
import com.sp5blue.shopshare.exceptions.shoppinglist.ListItemNotFoundException;
import com.sp5blue.shopshare.models.ListItem;
import com.sp5blue.shopshare.models.Shopper;
import com.sp5blue.shopshare.repositories.ListItemRepository;
import com.sp5blue.shopshare.services.shopper.ShopperService;

@ExtendWith(MockitoExtension.class)
class ListItemServiceTest {

    @Mock
    ListItemRepository mockListItemRepo;

    @Mock
    ShopperService mockShopperService;

    @InjectMocks
    ListItemService listItemService;



    @Test
    void create() {
        ListItem listItem = new ListItem("Hamburger");
        when(mockListItemRepo.save(listItem)).thenReturn(listItem);

        var result = listItemService.create(listItem);
        assertEquals(listItem, result);
    }

    @Test
    void readById_InvalidId_ThrowsListItemNotFoundException() {
        ListItem listItem = new ListItem("Hamburger");
        when(mockListItemRepo.findById(listItem.getId())).thenReturn(Optional.empty());

        var exception = assertThrows(ListItemNotFoundException.class, () -> listItemService.readById(listItem.getId()));
        assertEquals("List item does not exist - " + listItem.getId(), exception.getMessage());
    }

    @Test
    void readById_ValidId_ReturnsListItem() {
        ListItem listItem = new ListItem("Hamburger");
        when(mockListItemRepo.findById(listItem.getId())).thenReturn(Optional.of(listItem));

        var result = listItemService.readById(listItem.getId());
        assertEquals(listItem, result);
    }

    @Test
    void readByName_NoMatches_ReturnsEmptyList() {
        var result = listItemService.readByName("Name");
        assertTrue(result.isEmpty());
    }

    @Test
    void readByName_MultipleMatches_ReturnsMatches() {
        ListItem listItem1 = new ListItem("Pizza");
        ListItem listItem4 = new ListItem("Pizza");
        ListItem listItem5 = new ListItem("Pizza");
        when(mockListItemRepo.findAllByName("Pizza")).thenReturn(Arrays.asList(listItem1, listItem4, listItem5));

        var result = listItemService.readByName("Pizza");
        assertEquals(3, result.size());
        assertAll(
                () -> assertEquals(listItem1.getName(), result.get(0).getName()),
                () -> assertEquals(listItem4.getName(), result.get(1).getName()),
                () -> assertEquals(listItem5.getName(), result.get(2).getName())
        );
    }

    @Test
    void readByShopperId_InvalidId_ThrowsUserNotFoundException() {
        Shopper shopper = new Shopper("Jack", "Jill", "JackJill", "jack@email.com", "paswword");
        when(mockShopperService.shopperExists(shopper.getId())).thenReturn(false);

        var exception = assertThrows(UserNotFoundException.class, () -> listItemService.readByShopperId(shopper.getId()));
        assertEquals("Shopper with id " + shopper.getId() + " does not exist", exception.getMessage());
    }
    @Test
    void readByShopperId_ValidId_ReturnsListItems() {
        Shopper shopper = new Shopper("Jack", "Jill", "JackJill", "jack@email.com", "paswword");
        ListItem listItem1 = new ListItem("Tacos", shopper);
        ListItem listItem2 = new ListItem("Bananas", shopper);
        ListItem listItem3 = new ListItem("Pizza", shopper);
        ListItem listItem4 = new ListItem("Eggs", shopper);
        when(mockListItemRepo.findAllByCreatedBy_Id(shopper.getId())).thenReturn(Arrays.asList(listItem1, listItem2, listItem3, listItem4));
        when(mockShopperService.shopperExists(shopper.getId())).thenReturn(true);

        var results = listItemService.readByShopperId(shopper.getId());

        assertEquals(4, results.size());
        assertAll(
                () -> assertEquals("Tacos", results.get(0).getName()),
                () -> assertEquals("Bananas", results.get(1).getName()),
                () -> assertEquals("Pizza", results.get(2).getName()),
                () -> assertEquals("Eggs", results.get(3).getName())
        );
    }

}