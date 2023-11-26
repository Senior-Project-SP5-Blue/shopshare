package com.sp5blue.shopshare.exceptions.authentication;

public class UserAlreadyExistsException extends RuntimeException {
  public UserAlreadyExistsException(String message) {
    super(message);
  }

  public UserAlreadyExistsException() {}
}
