package com.sp5blue.shopshare.controllers.shoppinglist;

import com.sp5blue.shopshare.services.shoppinglist.IShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${api-prefix}/users/{user_id}/groups/{group_id}/shopping-lists")
public class ShoppingListController {
    private IShoppingListService shoppingListService;

    @Autowired
    public ShoppingListController(IShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping
    public ResponseEntity<?> getShoppingLists(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId) {
        return ResponseEntity.ok().body(shoppingListService.getShoppingListsByShopperGroupId(groupId));
    }

    @PostMapping
    public ResponseEntity<?> addShoppingList(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @RequestBody String name) {
        return ResponseEntity.ok().body(shoppingListService.createShoppingList(groupId, name));
    }

    @GetMapping("/{shopping-list_id}")
    public ResponseEntity<?> getShoppingList(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId) {
        return ResponseEntity.ok().body(shoppingListService.getShoppingListById(listId));
    }

    @PatchMapping("/{shopping-list_id}")
    public ResponseEntity<?> editShoppingList(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId, @RequestBody String name) {
        shoppingListService.changeShoppingListName(groupId, name);
        return ResponseEntity.ok().body("Shopping list name changed successfully.");
    }
}