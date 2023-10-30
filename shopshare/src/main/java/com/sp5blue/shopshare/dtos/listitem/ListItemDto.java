package com.sp5blue.shopshare.dtos.listitem;

import com.sp5blue.shopshare.models.listitem.ListItem;

import java.time.LocalDateTime;
import java.util.UUID;

public record ListItemDto(String id, String name, String status, UUID createdBy, Boolean locked, LocalDateTime createdOn) {
    public ListItemDto(ListItem listItem) {
        this(
                listItem.getId().toString(),
                listItem.getName(),
                listItem.getStatus().name(),
                listItem.getCreatedBy().getId(),
                listItem.isLocked(),
                listItem.getCreatedOn()
        );
    }
}
