package com.sp5blue.shopshare.models.listitem;

import java.util.UUID;

public record EditListItemDto(String name, ItemStatus status, boolean locked) {
}
