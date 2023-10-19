package com.sp5blue.shopshare.services.shoppinglist;

import com.sp5blue.shopshare.exceptions.shoppinglist.ListNotFoundException;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import com.sp5blue.shopshare.repositories.ShoppingListRepository;
import com.sp5blue.shopshare.services.shoppergroup.IShopperGroupService1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ShoppingListService1 implements IShoppingListService1 {
    private final ShoppingListRepository shoppingListRepository;

    private final IShopperGroupService1 shopperGroupService;


    @Autowired
    public ShoppingListService1(ShoppingListRepository shoppingListRepository, IShopperGroupService1 shopperGroupService) {
        this.shoppingListRepository = shoppingListRepository;
        this.shopperGroupService = shopperGroupService;
    }

    @Override
    @Transactional
    public ShoppingList createShoppingList(UUID userId, UUID groupId, String name) {
        ShopperGroup group = shopperGroupService.getShopperGroupById(userId, groupId);
        ShoppingList shoppingList = new ShoppingList(name, group);
        return shoppingListRepository.save(shoppingList);
    }

    @Override
    @Transactional
    public void changeShoppingListName(UUID userId, UUID groupId, UUID listId, String newName) {
        shopperGroupService.verifyUserHasGroup(userId, groupId);
        ShoppingList shoppingList = shoppingListRepository.findByGroup_IdAndId(groupId, listId).orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + listId));
        shoppingList.setName(newName);
    }

    @Override
    public ShoppingList getShoppingListById(UUID userId, UUID groupId, UUID listId) throws ListNotFoundException {
        shopperGroupService.verifyUserHasGroup(userId, groupId);
        return shoppingListRepository.findByGroup_IdAndId(groupId, listId).orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + listId));
    }

    @Override
    public List<ShoppingList> getShoppingLists(UUID userId, UUID groupId) {
        shopperGroupService.verifyUserHasGroup(userId, groupId);
        return shoppingListRepository.findAllByGroup_Id(groupId);
    }

    /*@Override
    @Transactional
    public boolean removeItemFromShoppingList(UUID userId, UUID groupId, UUID listId, UUID itemId) throws ListNotFoundException {
        ShoppingList shoppingList = shoppingListRepository.findByGroup_IdAndId(groupId, listId).orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + listId));
        return shoppingList.removeItem(itemId);
    }

    @Override
    @Transactional
    public boolean removeItemFromShoppingList(UUID userId, UUID listId, ListItem item) throws ListNotFoundException {
        ShoppingList shoppingList = shoppingListRepository.findById(listId).orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + listId));
        return shoppingList.removeItem(item);
    }

    @Override
    @Transactional
    public boolean removeItemToShoppingList(UUID userId, UUID groupId, UUID listId, UUID itemId) throws ListNotFoundException {
        ShoppingList shoppingList = shopperGroupService.getShopperGroupById(groupId)
                .getLists().stream().filter(l -> l.getId().equals(listId)).findFirst().orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + listId));
        return shoppingList.removeItem(itemId);
    }

    @Override
    @Transactional
    public boolean addItemToShoppingList(UUID userId, UUID listId, ListItem item) throws ListNotFoundException {
        ShoppingList shoppingList = shoppingListRepository.findById(listId).orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + listId));
        return shoppingList.addItem(item);
    }

    @Override
    @Transactional
    public boolean addItemToShoppingList(UUID userId, UUID groupId, UUID listId, ListItemDto listItemDto) throws ListNotFoundException {
            Shopper shopper = shopperService.readShopperById(userId);
            ShoppingList shoppingList = shopper
                    .getGroups().stream().filter(g -> g.getId().equals(groupId)).findFirst().orElseThrow(() -> new GroupNotFoundException("Shopper group does not exist - " + groupId))
                    .getLists().stream().filter(l -> l.getId().equals(listId)).findFirst().orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + listId));
            ListItem listItem = new ListItem(listItemDto.name(), listItemDto.status(), shopper, listItemDto.locked());
            return shoppingList.addItem(listItem);
    }

    @Override
    @Transactional
    public boolean addItemToShoppingList(UUID adminId, UUID listId, String name) throws ListNotFoundException {
        Shopper shopper = shopperService.readShopperById(adminId);
        ListItem item = new ListItem(name, shopper);
        ShoppingList shoppingList = shoppingListRepository.findById(listId).orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + listId));
        return shoppingList.addItem(item);
    }*/

    @Override
    public boolean shoppingListExistsById(UUID listId) {
        return shoppingListRepository.existsById(listId);
    }


    @Override
    public void verifyGroupHasList(UUID groupId, UUID listId) {
        shoppingListRepository.findByGroup_IdAndId(groupId, listId).orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + listId));
    }
}
