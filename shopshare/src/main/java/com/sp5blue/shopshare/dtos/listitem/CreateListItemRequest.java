package com.sp5blue.shopshare.dtos.listitem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateListItemRequest(
    @NotBlank(message = "Password cannot be blank")
        @Size(max = 500, message = "Password cannot exceed 500 characters")
        String name,
    boolean locked) {}
