package com.sp5blue.shopshare.services.shoppinglist;

import com.sp5blue.shopshare.exceptions.shoppergroup.GroupNotFoundException;
import com.sp5blue.shopshare.exceptions.shoppinglist.ListNotFoundException;
import com.sp5blue.shopshare.models.listitem.ListItem;
import com.sp5blue.shopshare.models.listitem.ListItemDto;
import com.sp5blue.shopshare.models.shopper.Shopper;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import com.sp5blue.shopshare.repositories.ShoppingListRepository;
import com.sp5blue.shopshare.services.shopper.IShopperService;
import com.sp5blue.shopshare.services.shoppergroup.IShopperGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ShoppingListService1 implements IShoppingListService {
    private final ShoppingListRepository shoppingListRepository;

    private final IShopperGroupService shopperGroupService;

    private final IShopperService shopperService;


    @Autowired
    public ShoppingListService1(ShoppingListRepository shoppingListRepository, IShopperGroupService shopperGroupService, IShopperService shopperService) {
        this.shoppingListRepository = shoppingListRepository;
        this.shopperGroupService = shopperGroupService;
        this.shopperService = shopperService;
    }

    @Override
    @Transactional
    public ShoppingList createShoppingList(ShopperGroup group, String name) {
        ShoppingList shoppingList = new ShoppingList(name, group);
        return shoppingListRepository.save(shoppingList);
    }
    @Override
    @Transactional
    public ShoppingList createShoppingList(UUID groupId, String name) {
        ShopperGroup group = shopperGroupService.getShopperGroupById(groupId);
        ShoppingList shoppingList = new ShoppingList(name, group);
        return shoppingListRepository.save(shoppingList);
    }
    @Override
    @Transactional
    public ShoppingList createShoppingList(ShoppingList shoppingList) {
        return shoppingListRepository.save(shoppingList);
    }

    @Override
    @Transactional
    public void changeShoppingListName(UUID shoppingListId, String newName) {
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId).orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + shoppingListId));
        shoppingList.setName(newName);
    }

    @Override
    @Transactional
    public void changeShoppingListName(UUID groupId, UUID shoppingListId, String newName) {
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId).orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + shoppingListId));
        shoppingList.setName(newName);
    }

    @Override
    public ShoppingList getShoppingListById(UUID id) throws ListNotFoundException {
        return shoppingListRepository.findById(id).orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + id));
    }

    @Override
    public List<ShoppingList> getShoppingListsByName(String name) {
        return shoppingListRepository.findAllByName(name);
    }

    @Override
    public List<ShoppingList> getShoppingListsByShopperGroupId(UUID groupId) throws GroupNotFoundException {
        boolean groupExists = shopperGroupService.shopperGroupExistsById(groupId);
        if (!groupExists) throw new GroupNotFoundException("Shopper group does not exist - " + groupId);
        return shoppingListRepository.findAllByGroup_Id(groupId);
    }

    @Override
    @Transactional
    public boolean removeItemFromShoppingList(UUID listId, UUID itemId) throws ListNotFoundException {
        ShoppingList shoppingList = shoppingListRepository.findById(listId).orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + listId));
        return shoppingList.removeItem(itemId);
    }

    @Override
    @Transactional
    public boolean removeItemFromShoppingList(UUID listId, ListItem item) throws ListNotFoundException {
        ShoppingList shoppingList = shoppingListRepository.findById(listId).orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + listId));
        return shoppingList.removeItem(item);
    }

    @Override
    @Transactional
    public boolean removeItemToShoppingList(UUID groupId, UUID listId, UUID itemId) throws ListNotFoundException {
        ShoppingList shoppingList = shopperGroupService.getShopperGroupById(groupId)
                .getLists().stream().filter(l -> l.getId().equals(listId)).findFirst().orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + listId));
        return shoppingList.removeItem(itemId);
    }

    @Override
    @Transactional
    public boolean addItemToShoppingList(UUID listId, ListItem item) throws ListNotFoundException {
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
    public boolean addItemToShoppingList(UUID creatorId, UUID listId, String name) throws ListNotFoundException {
        Shopper shopper = shopperService.readShopperById(creatorId);
        ListItem item = new ListItem(name, shopper);
        ShoppingList shoppingList = shoppingListRepository.findById(listId).orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + listId));
        return shoppingList.addItem(item);
    }

    @Override
    public boolean shoppingListExistsById(UUID listId) {
        return shoppingListRepository.existsById(listId);
    }
}
