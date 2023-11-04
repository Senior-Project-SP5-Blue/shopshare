package com.sp5blue.shopshare.security.request;

public record SignUpRequest (String username, String number, String firstName, String lastName, String email, String password) {}
