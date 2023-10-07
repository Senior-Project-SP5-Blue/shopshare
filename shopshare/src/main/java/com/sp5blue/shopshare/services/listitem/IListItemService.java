package com.sp5blue.shopshare.services.listitem;

import com.sp5blue.shopshare.exceptions.shoppinglist.ListNotFoundException;
import com.sp5blue.shopshare.models.ListItem;

import java.util.List;
import java.util.UUID;

public interface IListItemService {
    ListItem create(String name);

    ListItem create(ListItem listItem);

    ListItem readById(UUID id) throws ListNotFoundException;

    List<ListItem> readByName(String name);

    List<ListItem> readByShopperId(UUID shopperId);
}
