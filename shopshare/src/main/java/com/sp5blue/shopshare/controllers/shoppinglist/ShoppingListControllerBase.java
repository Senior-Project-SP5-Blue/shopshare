package com.sp5blue.shopshare.controllers.shoppinglist;

import com.sp5blue.shopshare.dtos.shoppinglist.CreateEditShoppingListRequest;
import com.sp5blue.shopshare.dtos.shoppinglist.ShoppingListDto;
import com.sp5blue.shopshare.dtos.shoppinglist.SlimShoppingListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Tag(name = "Shopping Lists", description = "Managing shopping lists shared across users in a group. Must be a part of group.")
public interface ShoppingListControllerBase {
    @Operation(
            summary = """
                    Get all shopping lists of a group"""
    )
    ResponseEntity<List<SlimShoppingListDto>> getShoppingLists(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId);

    @Operation(
            summary = """
                    Adds a new shopping list to a group"""
    )
    ResponseEntity<SlimShoppingListDto> addShoppingList(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @RequestBody CreateEditShoppingListRequest request);

    @Operation(
            summary = """
                    Get details about a specific shopping list. This includes all items in the list."""
    )
    ResponseEntity<ShoppingListDto> getShoppingList(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId);

    @Operation(
            summary = """
                    Modify a shopping list, i.e., change name"""
    )
    ResponseEntity<SlimShoppingListDto> editShoppingList(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId, @RequestBody CreateEditShoppingListRequest request);

    @Operation(
            summary = """
                    Delete shopping list. This will also delete all items in said list."""
    )
    ResponseEntity<?> deleteShoppingList(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId);
}
