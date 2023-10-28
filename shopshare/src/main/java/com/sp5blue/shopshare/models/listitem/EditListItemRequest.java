package com.sp5blue.shopshare.models.listitem;

public record EditListItemRequest(String name, ItemStatus status, boolean locked) {
}
