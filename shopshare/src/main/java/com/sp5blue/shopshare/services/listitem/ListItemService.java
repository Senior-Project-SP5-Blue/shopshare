package com.sp5blue.shopshare.services.listitem;

import com.sp5blue.shopshare.exceptions.shoppinglist.ListItemNotFoundException;
import com.sp5blue.shopshare.models.listitem.EditListItemDto;
import com.sp5blue.shopshare.models.listitem.ItemStatus;
import com.sp5blue.shopshare.models.listitem.ListItem;
import com.sp5blue.shopshare.models.listitem.CreateListItemDto;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import com.sp5blue.shopshare.repositories.ListItemRepository;
import com.sp5blue.shopshare.services.user.IUserService;
import com.sp5blue.shopshare.services.shoppergroup.IShopperGroupService;
import com.sp5blue.shopshare.services.shoppinglist.IShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ListItemService implements IListItemService {

    private final ListItemRepository listItemRepository;

    private final IShopperGroupService shopperGroupService;

    private final IShoppingListService shoppingListService;

    private final IUserService userService;


    @Autowired
    public ListItemService(ListItemRepository listItemRepository, IShopperGroupService shopperGroupService, IShoppingListService shoppingListService, IUserService userService) {
        this.listItemRepository = listItemRepository;
        this.shopperGroupService = shopperGroupService;
        this.shoppingListService = shoppingListService;
        this.userService = userService;
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<ListItem> addListItemToList(UUID userId, UUID groupId, UUID listId, CreateListItemDto createListItemDto) {
        CompletableFuture<User> getCreator = userService.getUserById(userId);
        CompletableFuture<ShoppingList> getShoppingList = shoppingListService.getShoppingListById(userId, groupId, listId);
        CompletableFuture.allOf(getCreator, getShoppingList).join();
        User creator = getCreator.join();
        ShoppingList shoppingList = getShoppingList.join();
        ListItem listItem = new ListItem(createListItemDto.name(), creator, shoppingList, createListItemDto.locked());
        shoppingList.setModifiedOn(LocalDateTime.now());
        shoppingList.setModifiedBy(creator);
        return CompletableFuture.completedFuture(listItemRepository.save(listItem));
    }

    @Override
    @Transactional
    @Async
    public void removeListItemFromList(UUID userId, UUID groupId, UUID listId, UUID itemId) throws ListItemNotFoundException {
        CompletableFuture<User> getUser = userService.getUserById(userId);
        CompletableFuture<ShoppingList> getShoppingList = shoppingListService.getShoppingListById(userId, groupId, listId);
        ListItem listItem = listItemRepository.findByList_IdAndId(listId, itemId).orElseThrow(() -> new ListItemNotFoundException("List item does not exist - " + itemId));
        CompletableFuture.allOf(getUser, getShoppingList).join();
        User user = getUser.join();
        ShoppingList shoppingList = getShoppingList.join();
        shoppingList.setModifiedOn(LocalDateTime.now());
        shoppingList.setModifiedBy(user);
        listItemRepository.delete(listItem);
    }

    @Override
    @Transactional
    @Async
    public void lockListItem(UUID userId, UUID groupId, UUID listId, UUID itemId) throws ListItemNotFoundException {
        shopperGroupService.verifyUserHasGroup(userId, groupId);
        shoppingListService.verifyGroupHasList(groupId, listId);
        ListItem listItem = listItemRepository.findByList_IdAndId(listId, itemId).orElseThrow(() -> new ListItemNotFoundException("List item does not exist - " + itemId));
        listItem.setLocked(true);
    }

    @Override
    @Transactional
    @Async
    public void unlockListItem(UUID userId, UUID groupId, UUID listId, UUID itemId) throws ListItemNotFoundException {
        shopperGroupService.verifyUserHasGroup(userId, groupId);
        shoppingListService.verifyGroupHasList(groupId, listId);
        ListItem listItem = listItemRepository.findByList_IdAndId(listId, itemId).orElseThrow(() -> new ListItemNotFoundException("List item does not exist - " + itemId));
        listItem.setLocked(false);
    }

    @Override
    @Transactional
    @Async
    public void markListItemAsComplete(UUID userId, UUID groupId, UUID listId, UUID itemId) throws ListItemNotFoundException {
        shopperGroupService.verifyUserHasGroup(userId, groupId);
        shoppingListService.verifyGroupHasList(groupId, listId);
        ListItem listItem = listItemRepository.findByList_IdAndId(listId, itemId).orElseThrow(() -> new ListItemNotFoundException("List item does not exist - " + itemId));
        listItem.setStatus(ItemStatus.COMPLETED);
    }

    @Override
    @Transactional
    @Async
    public void markListItemAsActive(UUID userId, UUID groupId, UUID listId, UUID itemId) throws ListItemNotFoundException {
        shopperGroupService.verifyUserHasGroup(userId, groupId);
        shoppingListService.verifyGroupHasList(groupId, listId);
        ListItem listItem = listItemRepository.findByList_IdAndId(listId, itemId).orElseThrow(() -> new ListItemNotFoundException("List item does not exist - " + itemId));
        listItem.setStatus(ItemStatus.ACTIVE);
    }

    @Override
    @Transactional
    @Async
    public void markListItemAsArchived(UUID userId, UUID groupId, UUID listId, UUID itemId) throws ListItemNotFoundException {
        shopperGroupService.verifyUserHasGroup(userId, groupId);
        shoppingListService.verifyGroupHasList(groupId, listId);
        ListItem listItem = listItemRepository.findByList_IdAndId(listId, itemId).orElseThrow(() -> new ListItemNotFoundException("List item does not exist - " + itemId));
        listItem.setStatus(ItemStatus.ARCHIVED);
    }

    @Override
    @Async
    public CompletableFuture<ListItem> getListItemById(UUID userId, UUID groupId, UUID listId, UUID itemId) throws ListItemNotFoundException {
        shopperGroupService.verifyUserHasGroup(userId, groupId);
        shoppingListService.verifyGroupHasList(groupId, listId);
        return CompletableFuture.completedFuture(listItemRepository.findByList_IdAndId(listId, itemId).orElseThrow(() -> new ListItemNotFoundException("List item does not exist - " + itemId)));
    }

    @Override
    @Async
    public CompletableFuture<List<ListItem>> getListItemsByCreator(UUID userId) {
        return CompletableFuture.completedFuture(listItemRepository.findAllByCreatedBy_Id(userId));
    }

    @Override
    @Async
    public CompletableFuture<List<ListItem>> getListItemsByShoppingList(UUID userId, UUID groupId, UUID listId) {
        shopperGroupService.verifyUserHasGroup(userId, groupId);
        shoppingListService.verifyGroupHasList(groupId, listId);
        return CompletableFuture.completedFuture(listItemRepository.findAllByList_Id(listId));
    }

    @Override
    @Transactional
    @Async
    public void removeListItemsFromList(UUID userId, UUID groupId, UUID listId) {
        ShoppingList shoppingList = shoppingListService.getShoppingListById(userId, groupId, listId).join();
        shoppingList.setItems(new ArrayList<>());
    }

    @Override
    @Transactional
    @Async
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
