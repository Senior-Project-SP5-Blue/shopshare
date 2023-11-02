package com.sp5blue.shopshare.controllers;

import java.time.LocalDateTime;

public record MiscExceptionErrorResponse(int status, String message, LocalDateTime timestamp) {
}