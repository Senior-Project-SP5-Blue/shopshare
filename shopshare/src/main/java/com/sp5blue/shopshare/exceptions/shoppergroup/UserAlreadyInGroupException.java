package com.sp5blue.shopshare.exceptions.shoppergroup;

public class UserAlreadyInGroupException extends RuntimeException {
  public UserAlreadyInGroupException(String message) {
    super(message);
  }

  public UserAlreadyInGroupException() {}
}
