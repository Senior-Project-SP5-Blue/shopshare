package com.sp5blue.shopshare.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
    @NotBlank(message = "Username cannot be blank")
        @Size(max = 50, message = "Username cannot exceed 500 characters")
        String username,
    @NotBlank(message = "First name cannot be blank")
        @Size(max = 35, message = "First name cannot exceed 35 characters")
        String firstName,
    @NotBlank(message = "Last name cannot be blank")
        @Size(max = 35, message = "Last name cannot exceed 35 characters")
        String lastName,
    @Email(message = "Must enter a valid email") String email,
    @NotBlank(message = "Number cannot be blank")
        @Size(max = 15, message = "Number cannot exceed 15 characters")
        String number,
    @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, max = 500, message = "Password must be between 6 and 500 characters")
        String password) {}
