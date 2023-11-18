package com.sp5blue.shopshare.controllers.listitem;

import com.sp5blue.shopshare.dtos.listitem.CreateListItemRequest;
import com.sp5blue.shopshare.dtos.listitem.EditListItemRequest;
import com.sp5blue.shopshare.dtos.listitem.ListItemDto;
import com.sp5blue.shopshare.services.listitem.IListItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${api-prefix}/users/{user_id}/groups/{group_id}/lists/{list_id}/items")
public class ListItemController implements ListItemControllerBase{
    private final IListItemService listItemService;

    public ListItemController(IListItemService listItemService) {
        this.listItemService = listItemService;
    }

    @Override
    @PostMapping
    public ResponseEntity<ListItemDto> addListItem(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("list_id") UUID listId, @RequestBody CreateListItemRequest createListItemRequest) {
        return ResponseEntity.ok().body(listItemService.addListItemToList(userId, groupId, listId, createListItemRequest).join());
    }

    @Override
    @DeleteMapping
    public ResponseEntity<?> deleteListItems(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("list_id") UUID listId) {
        listItemService.removeListItemsFromList(userId, groupId, listId);
        return ResponseEntity.ok().body("All items removed from list");
    }

    @Override
    @GetMapping("/{list-item_id}")
    public ResponseEntity<ListItemDto> getListItem(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("list_id") UUID listId, @PathVariable("list-item_id") UUID itemId) {
        return ResponseEntity.ok().body(listItemService.getListItemById(userId, groupId, listId, itemId).join());
    }

    @Override
    @PatchMapping("/{list-item_id}")
    public ResponseEntity<ListItemDto> changeListItem(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("list_id") UUID listId, @PathVariable("list-item_id") UUID itemId, @RequestBody EditListItemRequest editListItemRequest) {
        var editedItem = listItemService.editListItem(userId, groupId, listId, itemId, editListItemRequest).join();
        return ResponseEntity.ok().body(new ListItemDto(editedItem));
    }

    @Override
    @DeleteMapping("/{list-item_id}")
    public ResponseEntity<?> deleteListItem(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("list_id") UUID listId, @PathVariable("list-item_id") UUID itemId) {
        listItemService.removeListItemFromList(userId, groupId, listId, itemId);
        return ResponseEntity.ok().body("Item Removed Successfully");
    }
}
