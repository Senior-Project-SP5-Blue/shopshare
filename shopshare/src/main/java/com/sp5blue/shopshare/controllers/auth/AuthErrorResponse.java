package com.sp5blue.shopshare.controllers.auth;

import java.time.LocalDateTime;


    public record AuthErrorResponse(int status, String message, LocalDateTime timestamp) {
    }