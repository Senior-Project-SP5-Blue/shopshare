package com.sp5blue.shopshare.services.listitem;

import com.sp5blue.shopshare.exceptions.shoppinglist.ListItemNotFoundException;
import com.sp5blue.shopshare.models.listitem.EditListItemDto;
import com.sp5blue.shopshare.models.listitem.ItemStatus;
import com.sp5blue.shopshare.models.listitem.ListItem;
import com.sp5blue.shopshare.models.listitem.CreateListItemDto;
import com.sp5blue.shopshare.models.shopper.Shopper;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import com.sp5blue.shopshare.repositories.ListItemRepository;
import com.sp5blue.shopshare.services.shopper.IShopperService;
import com.sp5blue.shopshare.services.shoppergroup.IShopperGroupService;
import com.sp5blue.shopshare.services.shoppinglist.IShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ListItemService implements IListItemService {

    private final ListItemRepository listItemRepository;

    private final IShopperGroupService shopperGroupService;

    private final IShoppingListService shoppingListService;

    private final IShopperService shopperService;


    @Autowired
    public ListItemService(ListItemRepository listItemRepository, IShopperGroupService shopperGroupService, IShoppingListService shoppingListService, IShopperService shopperService) {
        this.listItemRepository = listItemRepository;
        this.shopperGroupService = shopperGroupService;
        this.shoppingListService = shoppingListService;
        this.shopperService = shopperService;
    }

    @Override
    @Transactional
    public ListItem addListItemToList(UUID userId, UUID groupId, UUID listId, CreateListItemDto createListItemDto) {
        Shopper creator = shopperService.getShopperById(userId);
        ShoppingList shoppingList = shoppingListService.getShoppingListById(userId, groupId, listId);
        ListItem listItem = new ListItem(createListItemDto.name(), creator, shoppingList, createListItemDto.locked());
        shoppingList.setModifiedOn(LocalDateTime.now());
        shoppingList.setModifiedBy(creator);
        return listItemRepository.save(listItem);
    }

    @Override
    @Transactional
    public void removeListItemFromList(UUID userId, UUID groupId, UUID listId, UUID itemId) throws ListItemNotFoundException {
        Shopper user = shopperService.getShopperById(userId);
        ShoppingList shoppingList = shoppingListService.getShoppingListById(userId, groupId, listId);
        ListItem listItem = listItemRepository.findByList_IdAndId(listId, itemId).orElseThrow(() -> new ListItemNotFoundException("List item does not exist - " + itemId));
        shoppingList.setModifiedOn(LocalDateTime.now());
        shoppingList.setModifiedBy(user);
        listItemRepository.delete(listItem);
    }

    @Override
    @Transactional
    public void lockListItem(UUID userId, UUID groupId, UUID listId, UUID itemId) throws ListItemNotFoundException {
        shopperGroupService.verifyUserHasGroup(userId, groupId);
        shoppingListService.verifyGroupHasList(groupId, listId);
        ListItem listItem = listItemRepository.findByList_IdAndId(listId, itemId).orElseThrow(() -> new ListItemNotFoundException("List item does not exist - " + itemId));
        listItem.setLocked(true);
    }

    @Override
    @Transactional
    public void unlockListItem(UUID userId, UUID groupId, UUID listId, UUID itemId) throws ListItemNotFoundException {
        shopperGroupService.verifyUserHasGroup(userId, groupId);
        shoppingListService.verifyGroupHasList(groupId, listId);
        ListItem listItem = listItemRepository.findByList_IdAndId(listId, itemId).orElseThrow(() -> new ListItemNotFoundException("List item does not exist - " + itemId));
        listItem.setLocked(false);
    }

    @Override
    @Transactional
    public void markListItemAsComplete(UUID userId, UUID groupId, UUID listId, UUID itemId) throws ListItemNotFoundException {
        shopperGroupService.verifyUserHasGroup(userId, groupId);
        shoppingListService.verifyGroupHasList(groupId, listId);
        ListItem listItem = listItemRepository.findByList_IdAndId(listId, itemId).orElseThrow(() -> new ListItemNotFoundException("List item does not exist - " + itemId));
        listItem.setStatus(ItemStatus.COMPLETED);
    }

    @Override
    @Transactional
    public void markListItemAsActive(UUID userId, UUID groupId, UUID listId, UUID itemId) throws ListItemNotFoundException {
        shopperGroupService.verifyUserHasGroup(userId, groupId);
        shoppingListService.verifyGroupHasList(groupId, listId);
        ListItem listItem = listItemRepository.findByList_IdAndId(listId, itemId).orElseThrow(() -> new ListItemNotFoundException("List item does not exist - " + itemId));
        listItem.setStatus(ItemStatus.ACTIVE);
    }

    @Override
    @Transactional
    public void markListItemAsArchived(UUID userId, UUID groupId, UUID listId, UUID itemId) throws ListItemNotFoundException {
        shopperGroupService.verifyUserHasGroup(userId, groupId);
        shoppingListService.verifyGroupHasList(groupId, listId);
        ListItem listItem = listItemRepository.findByList_IdAndId(listId, itemId).orElseThrow(() -> new ListItemNotFoundException("List item does not exist - " + itemId));
        listItem.setStatus(ItemStatus.ARCHIVED);
    }

    @Override
    public ListItem getListItemById(UUID userId, UUID groupId, UUID listId, UUID itemId) throws ListItemNotFoundException {
        shopperGroupService.verifyUserHasGroup(userId, groupId);
        shoppingListService.verifyGroupHasList(groupId, listId);
        return listItemRepository.findByList_IdAndId(listId, itemId).orElseThrow(() -> new ListItemNotFoundException("List item does not exist - " + itemId));
    }

    @Override
    public List<ListItem> getListItemsByCreator(UUID userId) {
        return listItemRepository.findAllByCreatedBy_Id(userId);
    }

    @Override
    public List<ListItem> getListItemsByShoppingList(UUID userId, UUID groupId, UUID listId) {
        shopperGroupService.verifyUserHasGroup(userId, groupId);
        shoppingListService.verifyGroupHasList(groupId, listId);
        return listItemRepository.findAllByList_Id(listId);
    }

    @Override
    @Transactional
    public void removeListItemsFromList(UUID userId, UUID groupId, UUID listId) {
        ShoppingList shoppingList = shoppingListService.getShoppingListById(userId, groupId, listId);
        shoppingList.setItems(new ArrayList<>());
    }

    @Override
    @Transactional
    public void editListItem(UUID userId, UUID groupId, UUID listId, UUID itemId, EditListItemDto editListItemDto) throws ListItemNotFoundException {
        shopperGroupService.verifyUserHasGroup(userId, groupId);
        shoppingListService.verifyGroupHasList(groupId, listId);
        ListItem listItem = listItemRepository.findByList_IdAndId(listId, itemId).orElseThrow(() -> new ListItemNotFoundException("List item does not exist - " + itemId));
        if (!itemId.equals(editListItemDto.id())) return;
        listItem.setName(editListItemDto.name());
        listItem.setStatus(editListItemDto.status());
        listItem.setLocked(editListItemDto.locked());
    }
}
