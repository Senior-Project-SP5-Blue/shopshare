package com.sp5blue.shopshare.security.request;

public record SignUpRequest (String username, String firstName, String lastName, String email, String number, String password) {}
