package com.sp5blue.shopshare.models.shoppinglist;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShoppingListDto(UUID id, String name, LocalDateTime modifiedOn, Number completed, int total) {
}
