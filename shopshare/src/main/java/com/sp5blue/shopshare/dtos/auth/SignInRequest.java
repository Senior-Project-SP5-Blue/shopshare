package com.sp5blue.shopshare.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
    @Email(message = "Must enter a valid email") String email,
    @NotBlank(message = "Password cannot be blank") String password) {}
