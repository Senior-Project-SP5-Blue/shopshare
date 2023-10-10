package com.sp5blue.shopshare.services.listitem;

import com.sp5blue.shopshare.exceptions.authentication.UserNotFoundException;
import com.sp5blue.shopshare.exceptions.shoppinglist.ListItemNotFoundException;
import com.sp5blue.shopshare.exceptions.shoppinglist.ListNotFoundException;
import com.sp5blue.shopshare.models.ListItem;
import com.sp5blue.shopshare.repositories.ListItemRepository;
import com.sp5blue.shopshare.services.shopper.IShopperService;
import com.sp5blue.shopshare.services.shopper.ShopperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ListItemService implements IListItemService {

    private final ListItemRepository listItemRepository;

    private final IShopperService shopperService;

    @Autowired
    public ListItemService(ListItemRepository listItemRepository, ShopperService shopperService) {
        this.listItemRepository = listItemRepository;
        this.shopperService = shopperService;
    }

    @Override
    @Transactional
    public ListItem create(String name) {
        ListItem listItem = new ListItem(name);
        return listItemRepository.save(listItem);
    }

    @Override
    @Transactional
    public ListItem create(ListItem listItem) {
        return listItemRepository.save(listItem);
    }

    @Override
    public ListItem readById(UUID id) throws ListNotFoundException {
        return listItemRepository.findById(id).orElseThrow(() -> new ListItemNotFoundException("List item does not exist - " + id));
    }

    @Override
    public List<ListItem> readByName(String name) {
        return listItemRepository.findAllByName(name);
    }

    @Override
    public List<ListItem> readByShopperId(UUID shopperId) {
        boolean shopperExists = shopperService.exists(shopperId);
        if (!shopperExists) throw new UserNotFoundException("Shopper with id " + shopperId + " does not exist");
        return listItemRepository.findAllByCreatedBy_Id(shopperId);
    }

}
