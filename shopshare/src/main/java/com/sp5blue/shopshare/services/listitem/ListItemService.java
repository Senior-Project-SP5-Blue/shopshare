package com.sp5blue.shopshare.services.listitem;

import com.sp5blue.shopshare.dtos.listitem.CreateListItemRequest;
import com.sp5blue.shopshare.dtos.listitem.EditListItemRequest;
import com.sp5blue.shopshare.dtos.listitem.ListItemDto;
import com.sp5blue.shopshare.exceptions.shoppergroup.InvalidUserPermissionsException;
import com.sp5blue.shopshare.exceptions.shoppinglist.ListItemNotFoundException;
import com.sp5blue.shopshare.models.listitem.ItemStatus;
import com.sp5blue.shopshare.models.listitem.ListItem;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.repositories.ListItemRepository;
import com.sp5blue.shopshare.services.shoppergroup.IShopperGroupService;
import com.sp5blue.shopshare.services.shoppinglist.IShoppingListService;
import com.sp5blue.shopshare.services.user.IUserService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListItemService implements IListItemService {

  private final ListItemRepository listItemRepository;

  private final IShopperGroupService shopperGroupService;

  private final IShoppingListService shoppingListService;

  private final IUserService userService;

  private final Logger logger = LoggerFactory.getLogger(IListItemService.class);

  @Autowired
  public ListItemService(
      ListItemRepository listItemRepository,
      IShopperGroupService shopperGroupService,
      IShoppingListService shoppingListService,
      IUserService userService) {
    this.listItemRepository = listItemRepository;
    this.shopperGroupService = shopperGroupService;
    this.shoppingListService = shoppingListService;
    this.userService = userService;
  }

  @Override
  @Transactional
  @Async
  public CompletableFuture<ListItemDto> addListItemToList(
      UUID userId, UUID groupId, UUID listId, CreateListItemRequest createListItemRequest) {
    CompletableFuture<User> getCreator = userService.getUserById(userId);
    CompletableFuture<ShoppingList> getShoppingList =
        shoppingListService.readShoppingListById(userId, groupId, listId);
    CompletableFuture.allOf(getCreator, getShoppingList).join();
    ShoppingList shoppingList = getShoppingList.join();
    User creator = getCreator.join();
    ListItem listItem =
        new ListItem(
            createListItemRequest.name(), creator, createListItemRequest.locked(), shoppingList);
    shoppingList.setModifiedOn(LocalDateTime.now());
    shoppingList.setModifiedBy(creator);
    var addedItem = listItemRepository.save(listItem);
    return CompletableFuture.completedFuture(new ListItemDto(addedItem));
  }

  @Override
  @Transactional
  @Async
  public CompletableFuture<ListItem> createListItem(
      UUID userId, UUID groupId, UUID listId, CreateListItemRequest createListItemRequest) {
    CompletableFuture<User> getCreator = userService.getUserById(userId);
    CompletableFuture<ShoppingList> getShoppingList =
        shoppingListService.readShoppingListById(userId, groupId, listId);
    CompletableFuture.allOf(getCreator, getShoppingList).join();
    ShoppingList shoppingList = getShoppingList.join();
    User creator = getCreator.join();
    ListItem listItem =
        new ListItem(
            createListItemRequest.name(), creator, createListItemRequest.locked(), shoppingList);
    shoppingList.setModifiedOn(LocalDateTime.now());
    shoppingList.setModifiedBy(creator);
    var addedItem = listItemRepository.save(listItem);
    return CompletableFuture.completedFuture(addedItem);
  }

  @Override
  @Transactional
  @Async
  public void removeListItemFromList(UUID userId, UUID groupId, UUID listId, UUID itemId)
      throws ListItemNotFoundException {
    CompletableFuture<User> getUser = userService.getUserById(userId);
    CompletableFuture<ShoppingList> getShoppingList =
        shoppingListService.readShoppingListById(userId, groupId, listId);
    CompletableFuture.allOf(getUser, getShoppingList).join();
    User user = getUser.join();
    ShoppingList shoppingList = getShoppingList.join();
    ListItem listItem =
        listItemRepository
            .findByList_IdAndId(listId, itemId)
            .orElseThrow(
                () -> new ListItemNotFoundException("List item does not exist - " + itemId));
    shoppingList.setModifiedOn(LocalDateTime.now());
    shoppingList.setModifiedBy(user);
    shoppingList.removeItem(listItem);
    listItemRepository.delete(listItem);
  }

  @Override
  @Transactional
  @Async
  public void removeListItemsFromList(UUID userId, UUID groupId, UUID listId) {
    ShoppingList shoppingList =
        shoppingListService.readShoppingListById(userId, groupId, listId).join();
    List<ListItem> items = shoppingList.getItems();
    shoppingList.setItems(new ArrayList<>());
    listItemRepository.deleteAll(items);
  }

  @Override
  @Transactional
  @Async
  public void lockListItem(UUID userId, UUID groupId, UUID listId, UUID itemId)
      throws ListItemNotFoundException {
    shopperGroupService.verifyUserHasGroup(userId, groupId);
    shoppingListService.verifyGroupHasList(groupId, listId);
    ListItem listItem =
        listItemRepository
            .findByList_IdAndId(listId, itemId)
            .orElseThrow(
                () -> new ListItemNotFoundException("List item does not exist - " + itemId));
    if (listItem.getCreatedBy().getId().equals(userId)) throw new InvalidUserPermissionsException("Cannot lock an item not created by yourself");
    listItem.setLocked(true);
  }

  @Override
  @Transactional
  @Async
  public void unlockListItem(UUID userId, UUID groupId, UUID listId, UUID itemId)
      throws ListItemNotFoundException {
    shopperGroupService.verifyUserHasGroup(userId, groupId);
    shoppingListService.verifyGroupHasList(groupId, listId);
    ListItem listItem =
        listItemRepository
            .findByList_IdAndId(listId, itemId)
            .orElseThrow(
                () -> new ListItemNotFoundException("List item does not exist - " + itemId));
    if (!listItem.getCreatedBy().getId().equals(userId)) throw new InvalidUserPermissionsException("Cannot unlock an item not created by yourself");
    listItem.setLocked(false);
  }

  @Override
  @Transactional
  @Async
  public void markListItemAsComplete(UUID userId, UUID groupId, UUID listId, UUID itemId)
      throws ListItemNotFoundException {
    shopperGroupService.verifyUserHasGroup(userId, groupId);
    shoppingListService.verifyGroupHasList(groupId, listId);
    ListItem listItem =
        listItemRepository
            .findByList_IdAndId(listId, itemId)
            .orElseThrow(
                () -> new ListItemNotFoundException("List item does not exist - " + itemId));
    listItem.setStatus(ItemStatus.COMPLETED);
  }

  @Override
  @Transactional
  @Async
  public void markListItemAsActive(UUID userId, UUID groupId, UUID listId, UUID itemId)
      throws ListItemNotFoundException {
    shopperGroupService.verifyUserHasGroup(userId, groupId);
    shoppingListService.verifyGroupHasList(groupId, listId);
    ListItem listItem =
        listItemRepository
            .findByList_IdAndId(listId, itemId)
            .orElseThrow(
                () -> new ListItemNotFoundException("List item does not exist - " + itemId));
    listItem.setStatus(ItemStatus.ACTIVE);
  }

  @Override
  @Transactional
  @Async
  public void markListItemAsArchived(UUID userId, UUID groupId, UUID listId, UUID itemId)
      throws ListItemNotFoundException {
    shopperGroupService.verifyUserHasGroup(userId, groupId);
    shoppingListService.verifyGroupHasList(groupId, listId);
    ListItem listItem =
        listItemRepository
            .findByList_IdAndId(listId, itemId)
            .orElseThrow(
                () -> new ListItemNotFoundException("List item does not exist - " + itemId));
    listItem.setStatus(ItemStatus.ARCHIVED);
  }

  @Transactional
  @Override
  @Async
  public CompletableFuture<ListItemDto> getListItemById(
      UUID userId, UUID groupId, UUID listId, UUID itemId) throws ListItemNotFoundException {
    shopperGroupService.verifyUserHasGroup(userId, groupId);
    shoppingListService.verifyGroupHasList(groupId, listId);
    var item =
        listItemRepository
            .findByList_IdAndId(listId, itemId)
            .orElseThrow(
                () -> new ListItemNotFoundException("List item does not exist - " + itemId));
    return CompletableFuture.completedFuture(new ListItemDto(item));
  }

  @Override
  @Async
  public CompletableFuture<ListItem> readListItemById(
      UUID userId, UUID groupId, UUID listId, UUID itemId) throws ListItemNotFoundException {
    shopperGroupService.verifyUserHasGroup(userId, groupId);
    shoppingListService.verifyGroupHasList(groupId, listId);
    var item =
        listItemRepository
            .findByList_IdAndId(listId, itemId)
            .orElseThrow(
                () -> new ListItemNotFoundException("List item does not exist - " + itemId));
    return CompletableFuture.completedFuture(item);
  }

  @Override
  @Transactional
  @Async
  public CompletableFuture<List<ListItemDto>> getListItemsByCreator(UUID userId) {
    var _items = listItemRepository.findAllByCreatedBy_Id(userId);
    var items = _items.stream().map(ListItemDto::new).toList();
    return CompletableFuture.completedFuture(items);
  }

  @Override
  @Async
  public CompletableFuture<List<ListItem>> readListItemsByCreator(UUID userId) {
    return CompletableFuture.completedFuture(listItemRepository.findAllByCreatedBy_Id(userId));
  }

  @Override
  @Transactional
  @Async
  public CompletableFuture<ListItem> editListItem(
      UUID userId, UUID groupId, UUID listId, UUID itemId, EditListItemRequest editListItemRequest)
      throws ListItemNotFoundException {
    shopperGroupService.verifyUserHasGroup(userId, groupId);
    shoppingListService.verifyGroupHasList(groupId, listId);
    ListItem listItem =
        listItemRepository
            .findByList_IdAndId(listId, itemId)
            .orElseThrow(
                () -> new ListItemNotFoundException("List item does not exist - " + itemId));
    listItem.setName(editListItemRequest.name());
    listItem.setStatus(editListItemRequest.status());
    listItem.setLocked(editListItemRequest.locked());
    return CompletableFuture.completedFuture(listItem);
  }
}
