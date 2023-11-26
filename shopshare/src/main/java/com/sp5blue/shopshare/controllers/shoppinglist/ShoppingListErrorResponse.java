package com.sp5blue.shopshare.controllers.shoppinglist;

import java.time.LocalDateTime;

public record ShoppingListErrorResponse(int status, String message, LocalDateTime timestamp) {}
