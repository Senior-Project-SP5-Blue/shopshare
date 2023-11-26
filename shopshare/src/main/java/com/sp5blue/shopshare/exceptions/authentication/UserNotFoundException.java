package com.sp5blue.shopshare.exceptions.authentication;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String message) {
    super(message);
  }

  public UserNotFoundException() {}
}
