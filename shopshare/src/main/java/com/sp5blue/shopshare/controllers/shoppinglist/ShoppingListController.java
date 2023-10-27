package com.sp5blue.shopshare.controllers.shoppinglist;

import com.sp5blue.shopshare.services.shoppinglist.IShoppingListService;
import io.micrometer.common.util.internal.logging.AbstractInternalLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${api-prefix}/users/{user_id}/groups/{group_id}/shopping-lists")
public class ShoppingListController {
    private final IShoppingListService shoppingListService;
    private final Logger logger = LoggerFactory.getLogger(ShoppingListController.class);

    private record ShoppingListName (String name) { }

    @Autowired
    public ShoppingListController(IShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping
    public ResponseEntity<?> getShoppingLists(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId) {
        return ResponseEntity.ok().body(shoppingListService.getShoppingLists(userId, groupId));
    }

    @PostMapping
    public ResponseEntity<?> addShoppingList(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @RequestBody ShoppingListName name) {
        logger.warn("Name is: {}", name.name);
        return ResponseEntity.ok().body(shoppingListService.createShoppingList(userId, groupId, name.name));
    }

    @GetMapping("/{shopping-list_id}")
    public ResponseEntity<?> getShoppingList(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId) {
        return ResponseEntity.ok().body(shoppingListService.getShoppingListById(userId, groupId, listId));
    }

    @PatchMapping("/{shopping-list_id}")
    public ResponseEntity<?> editShoppingList(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId, @RequestBody ShoppingListName name) {
        shoppingListService.changeShoppingListName(userId, groupId, listId, name.name);
        return ResponseEntity.ok().body("Shopping list name changed successfully.");
    }

    @DeleteMapping("/{shopping-list_id}")
    public ResponseEntity<?> deleteShoppingList(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId) {
        shoppingListService.deleteShoppingList(userId, groupId, listId);
        return ResponseEntity.ok().body("Successfully deleted shopping list");
    }
}