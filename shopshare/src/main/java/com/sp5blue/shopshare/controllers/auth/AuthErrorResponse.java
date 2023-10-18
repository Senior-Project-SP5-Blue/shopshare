package com.sp5blue.shopshare.controllers.auth;

import java.sql.Timestamp;
import java.time.LocalDateTime;


    public record AuthErrorResponse(int status, String message, LocalDateTime timestamp) {
    }