package com.sp5blue.shopshare.services.shoppinglist;

import com.sp5blue.shopshare.exceptions.shoppinglist.ListNotFoundException;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import com.sp5blue.shopshare.repositories.ShoppingListRepository;
import com.sp5blue.shopshare.services.shoppergroup.IShopperGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ShoppingListService implements IShoppingListService {
    private final ShoppingListRepository shoppingListRepository;

    private final IShopperGroupService shopperGroupService;

    @Autowired
    public ShoppingListService(ShoppingListRepository shoppingListRepository, IShopperGroupService shopperGroupService) {
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

    @Override
    public boolean shoppingListExistsById(UUID listId) {
        return shoppingListRepository.existsById(listId);
    }


    @Override
    public void verifyGroupHasList(UUID groupId, UUID listId) {
        shoppingListRepository.findByGroup_IdAndId(groupId, listId).orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + listId));
    }

    @Override
    @Transactional
    public void deleteShoppingList(UUID userId, UUID groupId, UUID listId) {
        shopperGroupService.verifyUserHasGroup(userId, groupId);
        ShoppingList shoppingList = shoppingListRepository.findByGroup_IdAndId(groupId, listId).orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + listId));
        shoppingListRepository.delete(shoppingList);
    }
}
