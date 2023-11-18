package com.sp5blue.shopshare.dtos.auth;

public record SignUpRequest (String username, String firstName, String lastName, String email, String number, String password) {}
