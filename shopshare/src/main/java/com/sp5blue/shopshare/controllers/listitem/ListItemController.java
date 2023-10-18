package com.sp5blue.shopshare.controllers.listitem;

import com.sp5blue.shopshare.models.listitem.ListItemDto;
import com.sp5blue.shopshare.services.listitem.IListItemService;
import com.sp5blue.shopshare.services.shopper.IShopperService;
import com.sp5blue.shopshare.services.shoppergroup.ShopperGroupService;
import com.sp5blue.shopshare.services.shoppinglist.IShoppingListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${api-prefix}/users/{user_id}/groups/{group_id}/shopping-lists/{shopping-list_id}/items")
public class ListItemController {
    private final IShopperService shopperService;
    private final ShopperGroupService shopperGroupService;
    private final IListItemService listItemService;

    private final IShoppingListService shoppingListService;

    public ListItemController(IShopperService shopperService, ShopperGroupService shopperGroupService, IListItemService listItemService, IShoppingListService shoppingListService) {
        this.shopperService = shopperService;
        this.shopperGroupService = shopperGroupService;
        this.listItemService = listItemService;
        this.shoppingListService = shoppingListService;
    }

    @GetMapping
    public ResponseEntity<?> getShoppingListItems(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId) {
        return ResponseEntity.ok().body(listItemService.getListItemsByShoppingList(listId));
    }
    @PostMapping
    public ResponseEntity<?> addListItem(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId, @RequestBody ListItemDto listItemDto) {
        shoppingListService.addItemToShoppingList(userId, groupId, listId, listItemDto);
        boolean added = shoppingListService.addItemToShoppingList(userId, groupId, listId, listItemDto);
        return ResponseEntity.ok().body(added ? "Item added successfully": "Item not added");
    }

    @GetMapping("/{list-item_id}")
    public ResponseEntity<?> getShoppingListItem(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopping-list_id") UUID listId, @PathVariable("list-item_id") UUID itemId) {
        return ResponseEntity.ok().body(listItemService.getListItemById(itemId));
    }

}
