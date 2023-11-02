package com.sp5blue.shopshare.controllers.listitem;

import com.sp5blue.shopshare.dtos.listitem.CreateListItemRequest;
import com.sp5blue.shopshare.dtos.listitem.EditListItemRequest;
import com.sp5blue.shopshare.dtos.listitem.ListItemDto;
import com.sp5blue.shopshare.security.accessannotations.GroupPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Tag(name = "List Items", description = "Managing items of a shopping list. DOES NOT INCLUDE GETTING ALL ITEMS OF A LIST - use shopping list route")
public interface ListItemControllerBase {
    @GroupPermission
    @Operation(
            summary = "Add item to a shopping list"
    )
    ResponseEntity<ListItemDto> addListItem(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId, @RequestBody CreateListItemRequest createListItemRequest);

    @GroupPermission
    @Operation(
            summary = "Delete ALL items from a shopping list"
    )
    ResponseEntity<?> deleteListItems(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId);

    @GroupPermission
    @Operation(
            summary = "Get a specific item from a shopping list"
    )
    ResponseEntity<ListItemDto> getListItem(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId, @PathVariable("list-item_id") UUID itemId);

    @GroupPermission
    @Operation(
            summary = "Modify an item from a shopping list, e.g. change name, change status, etc"
    )
    ResponseEntity<ListItemDto> changeListItem(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId, @PathVariable("list-item_id") UUID itemId, @RequestBody EditListItemRequest editListItemRequest);

    @GroupPermission
    @Operation(
            summary = "Delete an item from a shopping list"
    )
    ResponseEntity<?> deleteListItem(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId, @PathVariable("list-item_id") UUID itemId);
}
