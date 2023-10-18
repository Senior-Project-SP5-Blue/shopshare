package com.sp5blue.shopshare.services.listitem;

import com.sp5blue.shopshare.exceptions.authentication.UserNotFoundException;
import com.sp5blue.shopshare.exceptions.shoppinglist.ListItemNotFoundException;
import com.sp5blue.shopshare.exceptions.shoppinglist.ListNotFoundException;
import com.sp5blue.shopshare.models.listitem.ListItem;
import com.sp5blue.shopshare.models.listitem.ListItemDto;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import com.sp5blue.shopshare.repositories.ListItemRepository;
import com.sp5blue.shopshare.services.shopper.IShopperService;
import com.sp5blue.shopshare.services.shopper.ShopperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ListItemService1 implements IListItemService {

    private final ListItemRepository listItemRepository;

    private final IShopperService shopperService;


    @Autowired
    public ListItemService1(ListItemRepository listItemRepository, ShopperService shopperService) {
        this.listItemRepository = listItemRepository;
        this.shopperService = shopperService;
    }

    @Override
    @Transactional
    public ListItem createListItem(String name) {
        ListItem listItem = new ListItem(name);
        return listItemRepository.save(listItem);
    }

    @Override
    @Transactional
    public ListItem createListItem(ListItem listItem) {
        return listItemRepository.save(listItem);
    }

    @Override
    public ListItem getListItemById(UUID id) throws ListNotFoundException {
        return listItemRepository.findById(id).orElseThrow(() -> new ListItemNotFoundException("List item does not exist - " + id));
    }


    @Override
    public List<ListItem> getListItemsByShopper(UUID shopperId) {
        boolean shopperExists = shopperService.shopperExists(shopperId);
        if (!shopperExists) throw new UserNotFoundException("Shopper with id " + shopperId + " does not exist");
        return listItemRepository.findAllByCreatedBy_Id(shopperId);
    }

    @Override
    public List<ListItem> getListItemsByShoppingList(UUID listId) {
        return listItemRepository.findAllByList_Id(listId);
    }

    @Override
    public boolean addListItemToShoppingList(ShoppingList list, ListItemDto listItemDto) {
        return false;
    }


}
