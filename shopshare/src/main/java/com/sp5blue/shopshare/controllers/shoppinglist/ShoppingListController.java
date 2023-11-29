package com.sp5blue.shopshare.controllers.shoppinglist;

import com.sp5blue.shopshare.dtos.shoppinglist.CreateEditShoppingListRequest;
import com.sp5blue.shopshare.dtos.shoppinglist.ShoppingListDto;
import com.sp5blue.shopshare.dtos.shoppinglist.SlimShoppingListDto;
import com.sp5blue.shopshare.services.shoppinglist.IShoppingListService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api-prefix}/users/{user_id}/groups")
public class ShoppingListController implements ShoppingListControllerBase {
  private final IShoppingListService shoppingListService;
  private final Logger logger = LoggerFactory.getLogger(ShoppingListController.class);

  @Autowired
  public ShoppingListController(IShoppingListService shoppingListService) {
    this.shoppingListService = shoppingListService;
  }

  @Override
  @GetMapping("/lists")
  public ResponseEntity<List<SlimShoppingListDto>> getAllUsersShoppingLists(
      @PathVariable("user_id") UUID userId) {
    var lists = shoppingListService.getShoppingLists(userId).join();
    return ResponseEntity.ok().body(lists);
  }

  @Override
  @GetMapping("/{group_id}/lists")
  public ResponseEntity<List<SlimShoppingListDto>> getShoppingLists(
      @PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId) {
    var lists = shoppingListService.getShoppingLists(userId, groupId).join();
    return ResponseEntity.ok().body(lists);
  }

  @Override
  @PostMapping("/{group_id}/lists")
  public ResponseEntity<SlimShoppingListDto> addShoppingList(
      @PathVariable("user_id") UUID userId,
      @PathVariable("group_id") UUID groupId,
      @RequestBody @Valid CreateEditShoppingListRequest request) {
    var addedList =
        shoppingListService
            .addShoppingList(userId, groupId, request.name(), request.color())
            .join();
    return ResponseEntity.ok().body(addedList);
  }

  @Override
  @GetMapping("/{group_id}/lists/{list_id}")
  public ResponseEntity<ShoppingListDto> getShoppingList(
      @PathVariable("user_id") UUID userId,
      @PathVariable("group_id") UUID groupId,
      @PathVariable("list_id") UUID listId) {
    var list = shoppingListService.getShoppingListById(userId, groupId, listId).join();
    return ResponseEntity.ok().body(list);
  }

  @Override
  @PatchMapping("/{group_id}/lists/{list_id}")
  public ResponseEntity<SlimShoppingListDto> editShoppingList(
      @PathVariable("user_id") UUID userId,
      @PathVariable("group_id") UUID groupId,
      @PathVariable("list_id") UUID listId,
      @RequestBody @Valid CreateEditShoppingListRequest request) {
    var editedList =
        shoppingListService.changeShoppingListName(userId, groupId, listId, request.name()).join();
    return ResponseEntity.ok().body(editedList);
  }

  @Override
  @DeleteMapping("/{group_id}/lists/{list_id}")
  public ResponseEntity<?> deleteShoppingList(
      @PathVariable("user_id") UUID userId,
      @PathVariable("group_id") UUID groupId,
      @PathVariable("list_id") UUID listId) {
    shoppingListService.deleteShoppingList(userId, groupId, listId);
    return ResponseEntity.ok().body("Successfully deleted shopping list");
  }
}
