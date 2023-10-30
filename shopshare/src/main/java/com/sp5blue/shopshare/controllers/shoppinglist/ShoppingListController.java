package com.sp5blue.shopshare.controllers.shoppinglist;

import com.sp5blue.shopshare.dtos.shoppinglist.CreateEditShoppingListRequest;
import com.sp5blue.shopshare.dtos.shoppinglist.ShoppingListDto;
import com.sp5blue.shopshare.dtos.shoppinglist.SlimShoppingListDto;
import com.sp5blue.shopshare.services.shoppinglist.IShoppingListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api-prefix}/users/{user_id}/groups/{group_id}/shopping-lists")
public class ShoppingListController {
    private final IShoppingListService shoppingListService;
    private final Logger logger = LoggerFactory.getLogger(ShoppingListController.class);

    @Autowired
    public ShoppingListController(IShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping
    public ResponseEntity<List<SlimShoppingListDto>> getShoppingLists(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId) {
        var lists = shoppingListService.getShoppingLists(userId, groupId).join();
        return ResponseEntity.ok().body(lists);
    }

    @PostMapping
    public ResponseEntity<SlimShoppingListDto> addShoppingList(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @RequestBody CreateEditShoppingListRequest request) {
        var addedList = shoppingListService.addShoppingList(userId, groupId, request.name()).join();
        return ResponseEntity.ok().body(addedList);
    }

    @GetMapping("/{shopping-list_id}")
    public ResponseEntity<ShoppingListDto> getShoppingList(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId) {
        var list = shoppingListService.getShoppingListById(userId, groupId, listId).join();
        return ResponseEntity.ok().body(list);
    }

    @PatchMapping("/{shopping-list_id}")
    public ResponseEntity<SlimShoppingListDto> editShoppingList(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId, @RequestBody CreateEditShoppingListRequest request) {
        var editedList = shoppingListService.changeShoppingListName(userId, groupId, listId, request.name()).join();
        return ResponseEntity.ok().body(editedList);
    }

    @DeleteMapping("/{shopping-list_id}")
    public ResponseEntity<?> deleteShoppingList(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId) {
        shoppingListService.deleteShoppingList(userId, groupId, listId);
        return ResponseEntity.ok().body("Successfully deleted shopping list");
    }
}