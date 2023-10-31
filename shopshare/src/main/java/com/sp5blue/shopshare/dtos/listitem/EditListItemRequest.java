package com.sp5blue.shopshare.dtos.listitem;

import com.sp5blue.shopshare.models.listitem.ItemStatus;

public record EditListItemRequest(String name, ItemStatus status, boolean locked) {
}
