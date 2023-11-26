package com.sp5blue.shopshare.exceptions.token;

public class InvalidTokenException extends RuntimeException {
  public InvalidTokenException(String message) {
    super(message);
  }

  public InvalidTokenException() {}
}
