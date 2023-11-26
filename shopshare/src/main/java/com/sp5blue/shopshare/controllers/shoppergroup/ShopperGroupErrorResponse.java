package com.sp5blue.shopshare.controllers.shoppergroup;

import java.time.LocalDateTime;

public record ShopperGroupErrorResponse(int status, String message, LocalDateTime timestamp) {}
