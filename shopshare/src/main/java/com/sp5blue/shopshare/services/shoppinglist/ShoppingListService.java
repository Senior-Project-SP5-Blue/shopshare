package com.sp5blue.shopshare.services.shoppinglist;

import com.sp5blue.shopshare.exceptions.shoppinglist.ListNotFoundException;
import com.sp5blue.shopshare.models.ListItem;
import com.sp5blue.shopshare.models.Shopper;
import com.sp5blue.shopshare.models.ShopperGroup;
import com.sp5blue.shopshare.models.ShoppingList;
import com.sp5blue.shopshare.repositories.ShoppingListRepository;
import com.sp5blue.shopshare.services.listitem.IListItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ShoppingListService implements IShoppingListService {
    private final ShoppingListRepository shoppingListRepository;

    private final IListItemService listItemService;

    @Autowired
    public ShoppingListService(ShoppingListRepository shoppingListRepository, IListItemService listItemService) {
        this.shoppingListRepository = shoppingListRepository;
        this.listItemService = listItemService;
    }

    @Override
    @Transactional
    public ShoppingList create(String name, Shopper shopper) {
        ShoppingList shoppingList = new ShoppingList(name, shopper);
        return shoppingListRepository.save(shoppingList);
    }

    @Override
    @Transactional
    public ShoppingList create(String name, ShopperGroup group) {
        ShoppingList shoppingList = new ShoppingList(name, group);
        return shoppingListRepository.save(shoppingList);
    }
    @Override
    @Transactional
    public ShoppingList create(ShoppingList shoppingList) {
        return shoppingListRepository.save(shoppingList);
    }

    @Override
    public ShoppingList readById(UUID id) throws ListNotFoundException {
        return shoppingListRepository.findById(id).orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + id));
    }

    @Override
    public List<ShoppingList> readByName(String name) {
        return shoppingListRepository.findAllByName(name);
    }

    @Override
    public List<ShoppingList> readByShopperId(UUID shopperId) {
        return shoppingListRepository.findAllByShopper_Id(shopperId);
    }

    @Override
    public List<ShoppingList> readByShopperGroupId(UUID shopperGroupId) {
        return shoppingListRepository.findAllByGroup_Id(shopperGroupId);
    }

    @Override
    @Transactional
    public boolean removeItemFromList(UUID listId, UUID itemId) throws ListNotFoundException {
        ShoppingList shoppingList = shoppingListRepository.findById(listId).orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + listId));
        return shoppingList.removeItem(itemId);
    }

    @Override
    @Transactional
    public boolean removeItemFromList(UUID listId, ListItem item) throws ListNotFoundException {
        ShoppingList shoppingList = shoppingListRepository.findById(listId).orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + listId));
        return shoppingList.removeItem(item);
    }

    @Override
    @Transactional
    public boolean addItemToList(UUID listId, UUID itemId) throws ListNotFoundException {
        ShoppingList shoppingList = shoppingListRepository.findById(listId).orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + listId));
        ListItem listItem = listItemService.readById(itemId);
        return shoppingList.addItem(listItem);
    }

    @Override
    @Transactional
    public boolean addItemToList(UUID listId, ListItem item) throws ListNotFoundException {
        ShoppingList shoppingList = shoppingListRepository.findById(listId).orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + listId));
        return shoppingList.addItem(item);
    }
}
