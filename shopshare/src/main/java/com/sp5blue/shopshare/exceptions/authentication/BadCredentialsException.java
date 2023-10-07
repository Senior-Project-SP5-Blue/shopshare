package com.sp5blue.shopshare.exceptions.authentication;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException(String message) {
        super(message);
    }
}
