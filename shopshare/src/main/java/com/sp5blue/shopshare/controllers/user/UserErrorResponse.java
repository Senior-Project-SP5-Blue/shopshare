package com.sp5blue.shopshare.controllers.user;

import java.time.LocalDateTime;

public record UserErrorResponse(int status, String message, LocalDateTime timestamp) {
}
