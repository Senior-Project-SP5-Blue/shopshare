package com.sp5blue.shopshare.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
    @NotBlank String currentPassword,
    @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, max = 500, message = "Password must be between 6 and 500 characters")
        String newPassword,
    @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, max = 500, message = "Password must be between 6 and 500 characters")
        String confirmPassword) {}
