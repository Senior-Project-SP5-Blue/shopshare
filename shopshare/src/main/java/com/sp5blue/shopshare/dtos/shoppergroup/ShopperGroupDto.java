package com.sp5blue.shopshare.dtos.shoppergroup;

import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;

import java.util.List;
import java.util.UUID;

public record ShopperGroupDto(UUID id, String name, String admin, int users, List<slimList> lists) {
    private record slimList(UUID id, String name) {
        public slimList(ShoppingList list) {
            this (
                    list.getId(),
                    list.getName()
            );
        }
    }

    public ShopperGroupDto(ShopperGroup shopperGroup) {
        this (
                shopperGroup.getId(),
                shopperGroup.getName(),
                shopperGroup.getAdmin().getUsername(),
                shopperGroup.getUsers().size(),
                shopperGroup.getLists().stream().map(slimList::new).toList()
        );
    }
}
