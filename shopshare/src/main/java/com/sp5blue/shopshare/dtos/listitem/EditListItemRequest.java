package com.sp5blue.shopshare.dtos.listitem;

import com.sp5blue.shopshare.models.listitem.ItemStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EditListItemRequest(
    @NotBlank(message = "Name cannot be blank")
        @Size(max = 500, message = "Name cannot exceed 500 characters")
        String name,
    ItemStatus status,
    @NotNull(message = "Locked must be true or false") Boolean locked) {}
