package com.sp5blue.shopshare.dtos.shoppergroup;

import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import com.sp5blue.shopshare.models.user.User;

import java.util.List;
import java.util.UUID;

public record ShopperGroupDto(UUID id, String name, String admin, List<slimUser> users, List<slimList> lists) {
    private record slimUser(UUID id, String username) {
        public slimUser(User user) {
            this (
                    user.getId(),
                    user.getUsername()
            );
        }
    }
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
                shopperGroup.getUsers().stream().map(slimUser::new).toList(),
                shopperGroup.getLists().stream().map(slimList::new).toList()
        );
    }
}
