package com.sp5blue.shopshare.controllers.shopper;

import java.time.LocalDateTime;

public record ShopperErrorResponse(int status, String message, LocalDateTime timestamp) {
}
