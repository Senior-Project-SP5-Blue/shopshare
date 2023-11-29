package com.sp5blue.shopshare.dtos.shoppergroup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateEditShopperGroupRequest(
    @NotBlank(message = "Name cannot be blank")
        @Size(max = 500, message = "Name cannot exceed 500 characters")
        String name,
    String color) {}
