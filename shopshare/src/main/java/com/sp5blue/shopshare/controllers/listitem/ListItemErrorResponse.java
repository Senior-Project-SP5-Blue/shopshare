package com.sp5blue.shopshare.controllers.listitem;

import java.time.LocalDateTime;

public record ListItemErrorResponse(int status, String message, LocalDateTime timestamp) {}
