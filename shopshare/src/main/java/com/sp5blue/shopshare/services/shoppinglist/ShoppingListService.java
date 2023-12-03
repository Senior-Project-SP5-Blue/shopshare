package com.sp5blue.shopshare.services.shoppinglist;

import com.sp5blue.shopshare.dtos.shoppinglist.ShoppingListDto;
import com.sp5blue.shopshare.dtos.shoppinglist.SlimShoppingListDto;
import com.sp5blue.shopshare.exceptions.shoppinglist.ListNotFoundException;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import com.sp5blue.shopshare.repositories.ShoppingListRepository;
import com.sp5blue.shopshare.services.shoppergroup.IShopperGroupService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShoppingListService implements IShoppingListService {
  private final ShoppingListRepository shoppingListRepository;

  private final IShopperGroupService shopperGroupService;

  @Autowired
  public ShoppingListService(
      ShoppingListRepository shoppingListRepository, IShopperGroupService shopperGroupService) {
    this.shoppingListRepository = shoppingListRepository;
    this.shopperGroupService = shopperGroupService;
  }

  @Override
  @Transactional
  @Async
  public CompletableFuture<SlimShoppingListDto> addShoppingList(
      UUID userId, UUID groupId, String name) {
    ShopperGroup group = shopperGroupService.readShopperGroupById(userId, groupId).join();
    ShoppingList shoppingList = new ShoppingList(name, group);
    return CompletableFuture.completedFuture(
        new SlimShoppingListDto(shoppingListRepository.save(shoppingList)));
  }

  @Override
  @Transactional
  @Async
  public CompletableFuture<SlimShoppingListDto> addShoppingList(
      UUID userId, UUID groupId, String name, String color) {
    ShopperGroup group = shopperGroupService.readShopperGroupById(userId, groupId).join();
    ShoppingList shoppingList = new ShoppingList(name, group, color);
    return CompletableFuture.completedFuture(
        new SlimShoppingListDto(shoppingListRepository.save(shoppingList)));
  }

  @Override
  @Transactional
  @Async
  public CompletableFuture<ShoppingList> createShoppingList(
      UUID userId, UUID groupId, String name) {
    ShopperGroup group = shopperGroupService.readShopperGroupById(userId, groupId).join();
    ShoppingList shoppingList = new ShoppingList(name, group);
    return CompletableFuture.completedFuture(shoppingListRepository.save(shoppingList));
  }

  @Override
  @Transactional
  @Async
  public CompletableFuture<SlimShoppingListDto> changeShoppingList(
          UUID userId, UUID groupId, UUID listId, String newName, String color) {
    shopperGroupService.verifyUserHasGroup(userId, groupId);
    ShoppingList shoppingList =
        shoppingListRepository
            .findByGroup_IdAndId(groupId, listId)
            .orElseThrow(
                () -> new ListNotFoundException("Shopping list does not exist - " + listId));
    shoppingList.setName(newName);
    shoppingList.setColor(color);
    shoppingList.setModifiedOn(LocalDateTime.now());
    return CompletableFuture.completedFuture(new SlimShoppingListDto(shoppingList));
  }

  @Override
  @Transactional
  @Async
  public CompletableFuture<SlimShoppingListDto> changeShoppingList(
          UUID userId, UUID groupId, UUID listId, String newName) {
    shopperGroupService.verifyUserHasGroup(userId, groupId);
    ShoppingList shoppingList =
        shoppingListRepository
            .findByGroup_IdAndId(groupId, listId)
            .orElseThrow(
                () -> new ListNotFoundException("Shopping list does not exist - " + listId));
    shoppingList.setName(newName);
    shoppingList.setModifiedOn(LocalDateTime.now());
    return CompletableFuture.completedFuture(new SlimShoppingListDto(shoppingList));
  }

  @Override
  @Transactional
  @Async
  public CompletableFuture<ShoppingList> updateShoppingListName(
      UUID userId, UUID groupId, UUID listId, String newName) {
    shopperGroupService.verifyUserHasGroup(userId, groupId);
    ShoppingList shoppingList =
        shoppingListRepository
            .findByGroup_IdAndId(groupId, listId)
            .orElseThrow(
                () -> new ListNotFoundException("Shopping list does not exist - " + listId));
    shoppingList.setName(newName);
    shoppingList.setModifiedOn(LocalDateTime.now());
    return CompletableFuture.completedFuture(shoppingList);
  }

  @Override
  @Transactional
  @Async
  public CompletableFuture<ShoppingListDto> getShoppingListById(
      UUID userId, UUID groupId, UUID listId) throws ListNotFoundException {
    shopperGroupService.verifyUserHasGroup(userId, groupId);
    var list =
        shoppingListRepository
            .findByGroup_IdAndId(groupId, listId)
            .orElseThrow(
                () -> new ListNotFoundException("Shopping list does not exist - " + listId));
    return CompletableFuture.completedFuture(new ShoppingListDto(list));
  }

  @Override
  @Async
  public CompletableFuture<ShoppingList> readShoppingListById(
      UUID userId, UUID groupId, UUID listId) throws ListNotFoundException {
    shopperGroupService.verifyUserHasGroup(userId, groupId);
    var list =
        shoppingListRepository
            .findByGroup_IdAndId(groupId, listId)
            .orElseThrow(
                () -> new ListNotFoundException("Shopping list does not exist - " + listId));
    return CompletableFuture.completedFuture(list);
  }

  @Override
  @Async
  @Transactional
  public CompletableFuture<List<SlimShoppingListDto>> getShoppingLists(UUID userId, UUID groupId) {
    shopperGroupService.verifyUserHasGroup(userId, groupId);
    var _lists = shoppingListRepository.findAllByGroup_Id(groupId);
    var lists = _lists.stream().map(SlimShoppingListDto::new).toList();
    return CompletableFuture.completedFuture(lists);
  }

  @Override
  @Async
  @Transactional
  public CompletableFuture<List<SlimShoppingListDto>> getShoppingLists(UUID userId) {
    var _lists = shoppingListRepository.findAllBUser(userId);
    var lists = _lists.stream().map(SlimShoppingListDto::new).toList();
    return CompletableFuture.completedFuture(lists);
  }

  @Override
  @Async
  public CompletableFuture<List<ShoppingList>> readShoppingLists(UUID userId, UUID groupId) {
    shopperGroupService.verifyUserHasGroup(userId, groupId);
    var lists = shoppingListRepository.findAllByGroup_Id(groupId);
    return CompletableFuture.completedFuture(lists);
  }

  @Override
  @Async
  public CompletableFuture<Boolean> shoppingListExistsById(UUID listId) {
    return CompletableFuture.completedFuture(shoppingListRepository.existsById(listId));
  }

  @Override
  @Async
  public void verifyGroupHasList(UUID groupId, UUID listId) {
    shoppingListRepository
        .findByGroup_IdAndId(groupId, listId)
        .orElseThrow(() -> new ListNotFoundException("Shopping list does not exist - " + listId));
  }

  @Override
  @Transactional
  @Async
  public void deleteShoppingList(UUID userId, UUID groupId, UUID listId) {
    ShopperGroup shopperGroup = shopperGroupService.verifyUserHasGroup(userId, groupId);
    ShoppingList shoppingList =
        shoppingListRepository
            .findByGroup_IdAndId(groupId, listId)
            .orElseThrow(
                () -> new ListNotFoundException("Shopping list does not exist - " + listId));
    shopperGroup.removeList(listId);
    shoppingListRepository.delete(shoppingList);
  }
}
