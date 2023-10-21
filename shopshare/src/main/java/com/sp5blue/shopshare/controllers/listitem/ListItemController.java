package com.sp5blue.shopshare.controllers.listitem;

import com.sp5blue.shopshare.models.listitem.CreateListItemDto;
import com.sp5blue.shopshare.models.listitem.EditListItemDto;
import com.sp5blue.shopshare.services.listitem.IListItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${api-prefix}/users/{user_id}/groups/{group_id}/shopping-lists/{shopping-list_id}/items")
public class ListItemController {
    private final IListItemService listItemService;

    public ListItemController(IListItemService listItemService) {
        this.listItemService = listItemService;
    }

    @GetMapping
    public ResponseEntity<?> getListItems(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId) {
        return ResponseEntity.ok().body(listItemService.getListItemsByShoppingList(userId, groupId, listId).join());
    }

    @PostMapping
    public ResponseEntity<?> addListItem(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId, @RequestBody CreateListItemDto createListItemDto) {
        return ResponseEntity.ok().body(listItemService.addListItemToList(userId, groupId, listId, createListItemDto).join());
    }

    @DeleteMapping
    public ResponseEntity<?> deleteListItems(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId) {
        listItemService.removeListItemsFromList(userId, groupId, listId);
        return ResponseEntity.ok().body("All items removed from list");
    }

    @GetMapping("/{list-item_id}")
    public ResponseEntity<?> getListItem(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId, @PathVariable("list-item_id") UUID itemId) {
        return ResponseEntity.ok().body(listItemService.getListItemById(userId, groupId, listId, itemId).join());
    }

    @PatchMapping("/{list-item_id}")
    public ResponseEntity<?> changeListItem(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId, @PathVariable("list-item_id") UUID itemId, @RequestBody EditListItemDto editListItemDto) {
        listItemService.editListItem(userId, groupId, listId, itemId, editListItemDto);
        return ResponseEntity.ok().body("Successfully edited list item");
    }

    @DeleteMapping("/{list-item_id}")
    public ResponseEntity<?> deleteListItem(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId, @PathVariable("list-item_id") UUID itemId) {
        return ResponseEntity.ok().body(listItemService.getListItemById(userId, groupId, listId, itemId).join());
    }
}
