package com.sp5blue.shopshare.controllers.shoppinglist;

import com.sp5blue.shopshare.dtos.shoppinglist.CreateEditShoppingListRequest;
import com.sp5blue.shopshare.dtos.shoppinglist.ShoppingListDto;
import com.sp5blue.shopshare.dtos.shoppinglist.SlimShoppingListDto;
import com.sp5blue.shopshare.security.accessannotations.GroupPermission;
import com.sp5blue.shopshare.security.accessannotations.UserPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(
    name = "Shopping Lists",
    description =
        "Managing shopping lists shared across users in a group. Must be a part of group.")
public interface ShoppingListControllerBase {
  @UserPermission
  @Operation(summary = """
                    Get all shopping lists that a user has access to""")
  ResponseEntity<List<SlimShoppingListDto>> getAllUsersShoppingLists(
      @PathVariable("user_id") UUID userId);

  @GroupPermission
  @Operation(summary = """
                    Get all shopping lists of a group""")
  ResponseEntity<List<SlimShoppingListDto>> getShoppingLists(
      @PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId);

  @GroupPermission
  @Operation(summary = """
                    Adds a new shopping list to a group""")
  ResponseEntity<SlimShoppingListDto> addShoppingList(
      @PathVariable("user_id") UUID userId,
      @PathVariable("group_id") UUID groupId,
      @RequestBody CreateEditShoppingListRequest request);

  @GroupPermission
  @Operation(
      summary =
          """
                    Get details about a specific shopping list. This includes all items in the list.""")
  ResponseEntity<ShoppingListDto> getShoppingList(
      @PathVariable("user_id") UUID userId,
      @PathVariable("group_id") UUID groupId,
      @PathVariable("shopping-list_id") UUID listId);

  @GroupPermission
  @Operation(summary = """
                    Modify a shopping list, i.e., change name""")
  ResponseEntity<SlimShoppingListDto> editShoppingList(
      @PathVariable("user_id") UUID userId,
      @PathVariable("group_id") UUID groupId,
      @PathVariable("shopping-list_id") UUID listId,
      @RequestBody CreateEditShoppingListRequest request);

  @GroupPermission
  @Operation(
      summary =
          """
                    Delete shopping list. This will also delete all items in said list.""")
  ResponseEntity<?> deleteShoppingList(
      @PathVariable("user_id") UUID userId,
      @PathVariable("group_id") UUID groupId,
      @PathVariable("shopping-list_id") UUID listId);
}
